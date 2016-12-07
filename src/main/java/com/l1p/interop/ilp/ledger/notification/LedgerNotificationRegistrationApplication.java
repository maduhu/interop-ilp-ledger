package com.l1p.interop.ilp.ledger.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1p.interop.ilp.ledger.notification.domain.*;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class LedgerNotificationRegistrationApplication extends WebSocketApplication {
  private static final Logger log = LoggerFactory.getLogger(LedgerNotificationRegistrationApplication.class);

  private ConcurrentHashMap<String, Set<WebSocket>> subscriptions;
  private final ObjectMapper mapper;
  private final Broadcaster broadcaster;

  public LedgerNotificationRegistrationApplication() {
    subscriptions = new ConcurrentHashMap<String, Set<WebSocket>>();
    mapper = new ObjectMapper();
    broadcaster = new OptimizedBroadcaster();
  }


  @Override
  public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket requestPacket, WebSocketListener... listeners) {
    return new LedgerNotificationWebSocket(handler, requestPacket, listeners);
  }

  @Override
  public void onMessage(WebSocket socket, String text) {
    // expect to receive registration request only
    handleNotificationSubscriptionRequest(socket, text);
  }

  @Override
  public void onClose(WebSocket socket, DataFrame frame) {
    // remove socket from the list
    final LedgerNotificationWebSocket ledgerNotificationWebSocket = (LedgerNotificationWebSocket) socket;
    for (String account : ledgerNotificationWebSocket.getAccounts()) {
      final Set<WebSocket> webSockets = subscriptions.get(account);
      final boolean remove = webSockets.remove(ledgerNotificationWebSocket);
      log.info("websocket for account {} removed:{} onClose()", account, remove);
      subscriptions.computeIfPresent(account, (key, webSocketFromMap) -> {
        if (webSocketFromMap.isEmpty()) return null;
        else return webSocketFromMap;
      });
    }
  }

  private void broadcast(String account, String text) {
    final Set<WebSocket> subscriptions = this.subscriptions.get(account);
    if (subscriptions != null && !subscriptions.isEmpty()) {
      broadcaster.broadcast(subscriptions, text);
      log.info("account: {} message send to websockets", account);
    } else {
      log.warn("no one subscribed for account: {}", account);
    }
  }

  private void handleNotificationSubscriptionRequest(final WebSocket socket, String text) {
    try {
      final SubscriptionRequest subscriptionRequest = mapper.readValue(text, SubscriptionRequest.class);
      LedgerNotificationWebSocket ledgerNotificationWebSocket = (LedgerNotificationWebSocket) socket;
      ledgerNotificationWebSocket.setAccounts(subscriptionRequest.getParams().getAccounts());

      // save the websocket by account
      for (String account : subscriptionRequest.getParams().getAccounts()) {
        Set<WebSocket> existingWebSockets = null;
        Set<WebSocket> mergedWebSockets = null;

        do {
          Set<WebSocket> newWebSockets = new CopyOnWriteArraySet<WebSocket>();
          newWebSockets.add(socket);
          existingWebSockets = subscriptions.putIfAbsent(account, newWebSockets);
          if (existingWebSockets != null) { // check if the account has exisiting subscribing websockets

            // mergedWebSockets would be non-null if we merge sockets successfully
            mergedWebSockets = subscriptions.computeIfPresent(account, (s, webSocketFromMap) -> {
              webSocketFromMap.add(socket);
              return webSocketFromMap;
            });
          }
        } while (!(existingWebSockets == null || mergedWebSockets != null));
      }

      // successfully added subscription
      final SubscriptionResponse subscriptionResponse = new SubscriptionResponse(subscriptionRequest.getId(), subscriptionRequest.getJsonrpc(), subscriptionRequest.getParams().getAccounts().size());
      socket.send(mapper.writeValueAsString(subscriptionResponse));

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void sendTransferPreparedNotification(Transfer transfer) {
    sendTransferNotification(TransferParams.TRANSFER_CREATE, transfer);
  }

  public void sendTranferExecutedNotification(Transfer transfer) {
      sendTransferNotification(TransferParams.TRANSFER_UPDATE, transfer);
  }

  private void sendTransferNotification(String transferType, Transfer transfer) {
    try {
      final Notification notification = new Notification();
      TransferParams params = new TransferParams(transferType, transfer);
      notification.setParams(params);
      String notificationJson = mapper.writeValueAsString(notification);
      for (Credit credit : transfer.getCredits()) {
        broadcast(credit.getAccount(), notificationJson);
      }
      for (Debit debit : transfer.getDebits()) {
        broadcast(debit.getAccount(), notificationJson);
      }
    } catch (JsonProcessingException e) {
      log.warn("Failed to send transfer prepared notification", e);
    }
  }

  public void sendMessageNotification(String account, String msg) {
    broadcast(account, msg);
  }

}
