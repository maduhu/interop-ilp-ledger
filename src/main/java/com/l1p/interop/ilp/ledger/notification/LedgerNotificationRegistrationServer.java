package com.l1p.interop.ilp.ledger.notification;

import com.l1p.interop.ilp.ledger.notification.domain.Credit;
import com.l1p.interop.ilp.ledger.notification.domain.Transfer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class LedgerNotificationRegistrationServer {
  private static final Logger log = LoggerFactory.getLogger(LedgerNotificationRegistrationServer.class);

  public LedgerNotificationRegistrationServer(String staticContentDocRoot, int port, String webSocketPath, WebSocketApplication ledgerNotificationRegistrationApplication) {
    startServer(staticContentDocRoot, port, webSocketPath, ledgerNotificationRegistrationApplication);
  }

  public void startServer(String docRoot, int port, String path, WebSocketApplication ledgerNotificationRegistrationApplication) {
    final HttpServer server = HttpServer.createSimpleServer(docRoot, port);
    final WebSocketAddOn addon = new WebSocketAddOn();
    for (NetworkListener listener : server.getListeners()) {
      listener.registerAddOn(addon);
    }

    server.getListener("grizzly").registerAddOn(new WebSocketAddOn());
    WebSocketEngine.getEngine().register("", path, ledgerNotificationRegistrationApplication);

    try {
      server.start();
    } catch (IOException e) {
      final String msg = "Failed to start notification server";
      log.error(msg, e);
      throw new RuntimeException(msg, e);
    }
  }

  public void shutdownServer() {
    log.info("need to shutdown server");
  }

  public static void main(String[] args) throws Exception {
    final LedgerNotificationRegistrationApplication app = new LedgerNotificationRegistrationApplication();
    final LedgerNotificationRegistrationServer server = new LedgerNotificationRegistrationServer("/tmp", 10001, "/websocket", app);
    Thread notificationSender = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          for (int i = 0; i < 100; i++) {
            Thread.sleep(5000);
            final Transfer transfer = new Transfer();
            final Credit credit = new Credit("https://ledger.example/accounts/alice", "100", "memo: ninguna vez");
            transfer.setCredits(new ArrayList<Credit>(){{add(credit);}});
            app.sendTransferPreparedNotification(transfer);
            log.info("sent prepared transfer");
          }
        } catch (InterruptedException e) {
          log.error("screwed up", e);
        }
      }
    });
    notificationSender.start();
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        server.shutdownServer();
      }
    }));
    System.in.read();
  }
}
