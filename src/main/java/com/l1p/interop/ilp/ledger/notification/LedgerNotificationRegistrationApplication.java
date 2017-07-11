package com.l1p.interop.ilp.ledger.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.l1p.interop.ilp.ledger.LedgerUrlMapper;
import com.l1p.interop.ilp.ledger.domain.*;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang.exception.ExceptionUtils;

public class LedgerNotificationRegistrationApplication extends WebSocketApplication {
	private static final Logger log = LoggerFactory.getLogger(LedgerNotificationRegistrationApplication.class);

	private static final String CONNECTION_HANDSHAKE_MESSAGE = "{ \"jsonrpc\": \"2.0\", \"method\": \"connect\", \"id\": null}";

	private ConcurrentHashMap<String, Set<WebSocket>> subscriptions;
	private final ObjectMapper mapper;
	private final Broadcaster broadcaster;
	private final LedgerUrlMapper ledgerUrlMapper;
	private boolean subscribeAllAccounts;

	public LedgerNotificationRegistrationApplication(LedgerUrlMapper ledgerUrlMapper) {
		this.ledgerUrlMapper = ledgerUrlMapper;
		subscriptions = new ConcurrentHashMap<String, Set<WebSocket>>();
		mapper = new ObjectMapper();
		broadcaster = new OptimizedBroadcaster();
	}

	@Override
	public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket requestPacket,
			WebSocketListener... listeners) {
		// on validation failure throw Handshake exception
		log.info("Received connection request from {}", requestPacket.getRemoteAddress());
		return new LedgerNotificationWebSocket(handler, requestPacket, listeners);
	}

	@Override
	public void onConnect(WebSocket socket) {
		super.onConnect(socket);
		socket.send(CONNECTION_HANDSHAKE_MESSAGE);
		log.info("sent connect response: {}", CONNECTION_HANDSHAKE_MESSAGE);
	}

	@Override
	public void onMessage(WebSocket socket, String text) {
		// expect to receive registration request only
		log.info("received message: {}", text);
		try {
			final SubscriptionRequest subscriptionRequest = mapper.readValue(text, SubscriptionRequest.class);
			if(subscriptionRequest.getMethod().equalsIgnoreCase("subscribe_account")){
				handleSubscribeToAccountNotificationRequest(socket, text);
			} else if(subscriptionRequest.getMethod().equalsIgnoreCase("subscribe_all_accounts")) {
				handleSubscribeAllAccountsNotificationRequest(socket, text);
			}
		} catch (IOException e) {
			log.warn("In onMessage method. Failed converting from string request to SubscriptionRequest class: {}", ExceptionUtils.getStackTrace(e));
		}

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
				if (webSocketFromMap.isEmpty())
					return null;
				else
					return webSocketFromMap;
			});
		}
		super.onClose(socket, frame);
	}

	@Override
	protected boolean onError(WebSocket webSocket, Throwable t) {
		log.error("Error on websocket", t);
		return true;
	}

	private void broadcast(String account, String text) {
		subscriptions.forEach((k,v) -> log.info("Accounts subscribed for: "+k));
        final Set<WebSocket> subscriptions = this.subscriptions.get(account);
        if (subscriptions != null && !subscriptions.isEmpty()) {
            broadcaster.broadcast(subscriptions, text);
            log.info("account: {} message send to websockets", account);
        } else {
            log.warn("no one subscribed for account: {}", account);
        }
	}

    private void broadcastAll( String text) {
        subscriptions.forEach((account,socketList) -> {
            final Set<WebSocket> subscriptions = this.subscriptions.get(account);
            if (subscriptions != null && !subscriptions.isEmpty()) {
                broadcaster.broadcast(subscriptions, text);
                log.info("account: {} message send to websockets", account);
            } else {
                log.warn("no one subscribed for account: {}", account);
            }

        });
    }

	private void handleSubscribeToAccountNotificationRequest(final WebSocket socket, String text) {
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
					if (existingWebSockets != null) { // check if the account has existing subscribing websockets

						// mergedWebSockets would be non-null if we merge sockets successfully
						mergedWebSockets = subscriptions.computeIfPresent(account, (s, webSocketFromMap) -> {
							webSocketFromMap.add(socket);
							return webSocketFromMap;
						});
					}
				} while (!(existingWebSockets == null || mergedWebSockets != null));
			}

			// successfully added subscription
			final SubscriptionResponse subscriptionResponse = new SubscriptionResponse(subscriptionRequest.getId(),
					subscriptionRequest.getJsonrpc(), subscriptionRequest.getParams().getAccounts().size());
			String subscriptionResponseJson = mapper.writeValueAsString(subscriptionResponse);
			log.info("send response to subscription request: {} ", subscriptionResponseJson);
			socket.send(subscriptionResponseJson);
			// socket.send("{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":1}");

		} catch (IOException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}

	}

	private void handleSubscribeAllAccountsNotificationRequest(final WebSocket socket, String text) {
		try {
			subscribeAllAccounts = true;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
	}


	public void sendTransferPreparedNotification(String transferJson) {
		try {
			log.info("Prepared Transfer JSON: {}", transferJson);
			final Transfer transfer = mapper.readValue(transferJson, Transfer.class);
			//ledgerUrlMapper.mapUrlToLedgerAdapter(transfer);
			sendTransferPreparedNotification(transfer);
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert to Transfer", e);
		}
	}

	public void sendTranferExecutedNotification(String transferJson,String fulfillmentCondition) {
		try {
			log.info("Executed Transfer JSON: {}", transferJson);
			final Transfer transfer = mapper.readValue(transferJson, Transfer.class);
			ledgerUrlMapper.mapUrlToLedgerAdapter(transfer);
			sendTranferExecutedNotification(transfer,fulfillmentCondition);
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert to Transfer", e);
		}
	}
	
	public void sendTranferRejectedNotification(String transferJson) {
		try {
			log.info("Rejected Transfer JSON: {}", transferJson);
			final Transfer transfer = mapper.readValue(transferJson, Transfer.class);
			ledgerUrlMapper.mapUrlToLedgerAdapter(transfer);
			sendTranferRejectedNotification(transfer);
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert to Transfer", e);
		}
	}

	public void sendTransferPreparedNotification(Transfer transfer) {
		sendTransferNotification(TransferParams.TRANSFER_CREATE, transfer,new HashMap<String, String>());
	}

	public void sendTranferExecutedNotification(Transfer transfer,String fulfillmentCondition) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("execution_condition_fulfillment", fulfillmentCondition);
		sendTransferNotification(TransferParams.TRANSFER_UPDATE, transfer, map);
	}
	
	public void sendTranferRejectedNotification(Transfer transfer) {
		sendTransferNotification(TransferParams.TRANSFER_UPDATE, transfer,new HashMap<String, String>());
	}

	private void sendTransferNotification(String transferType, Transfer transfer,HashMap<String,String> relatedResourceMap) {
		try {
			log.warn("Received notification for publishing");
			final Notification notification = new Notification();
			TransferParams params = new TransferParams(transferType, transfer);
			params.setRelatedResources(relatedResourceMap);
			notification.setParams(params);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String notificationJson = mapper.writeValueAsString(notification);
			log.info("notification: " + notificationJson);
			if(subscribeAllAccounts){
                broadcastAll(notificationJson);
            } else {
                for (Credit credit : transfer.getCredits()) {
                    broadcast(credit.getAccount(), notificationJson);
                }
                for (Debit debit : transfer.getDebits()) {
                    broadcast(debit.getAccount(), notificationJson);
                }
            }
		} catch (JsonProcessingException e) {
			log.warn("Failed to send transfer prepared notification", e);
		}
	}

	public void sendMessageNotification(String msgJson) {
		// broadcast(account, msg);
		try {
			log.info("Message JSON: {}", msgJson);
			final Message message = mapper.readValue(msgJson, Message.class);
			// ledgerUrlMapper.mapUrlToLedgerAdapter(message);
			final Notification notification = new Notification();
			MessageParams params = new MessageParams("message.send", message);
			notification.setParams(params);
			String notificationJson = mapper.writeValueAsString(notification);
			broadcast(message.getTo(), notificationJson);

		} catch (IOException e) {
			throw new RuntimeException("Failed to convert to Transfer", e);
		}
	}
	
	

	/**
	 * Used for unit testing only.
	 */
	public void setSubscriptions(ConcurrentHashMap<String, Set<WebSocket>> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	
	/**
	 * Used for unit testing only.
	 */
	public boolean onErrorForTesting(WebSocket webSocket, Throwable t) {
		return this.onError(webSocket, t);
	}


}
