package com.l1p.interop.ilp.ledger.notification;

import com.l1p.interop.ilp.ledger.LedgerUrlMapper;
import com.l1p.interop.ilp.ledger.domain.Credit;
import com.l1p.interop.ilp.ledger.domain.Transfer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class LedgerNotificationRegistrationServer implements org.mule.api.lifecycle.Lifecycle {
  private static final Logger log = LoggerFactory.getLogger(LedgerNotificationRegistrationServer.class);
  private HttpServer server;
  private String docRoot;
  private int port;
  private String path;
  private WebSocketApplication ledgerNotificationRegistrationApplication;

  public LedgerNotificationRegistrationServer(String staticContentDocRoot, int port, String webSocketPath, WebSocketApplication ledgerNotificationRegistrationApplication) {
    this.docRoot = staticContentDocRoot;
    this.port = port;
    this.path = webSocketPath;
    this.ledgerNotificationRegistrationApplication = ledgerNotificationRegistrationApplication;
  }

  @Override
  public void initialise() throws InitialisationException {
    server = HttpServer.createSimpleServer(docRoot, port);
    final WebSocketAddOn addon = new WebSocketAddOn();
    for (NetworkListener listener : server.getListeners()) {
      listener.registerAddOn(addon);
    }

    server.getListener("grizzly").registerAddOn(new WebSocketAddOn());
    WebSocketEngine.getEngine().register("", path, ledgerNotificationRegistrationApplication);
    log.info("Initialized websocket server");
  }

  @Override
  public void start() throws MuleException {
    try {
      server.start();
    } catch (IOException e) {
      final String msg = "Failed to start notification server";
      log.error(msg, e);
      throw new LifecycleException(e, this);
    }
    log.info("Started websocket server");
  }

  @Override
  public void stop() throws MuleException {
    log.info("Shutting down grizzly websocket server");
    if (server.isStarted()) server.shutdown();
  }

  @Override
  public void dispose() {
  }

  public static void main(String[] args) throws Exception {
    final LedgerNotificationRegistrationApplication app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper(".*/ledger/", "http://0.0.0.0/ledger/base/path"));
    final LedgerNotificationRegistrationServer server = new LedgerNotificationRegistrationServer("/tmp", 10001, "/websocket", app);
    server.initialise();
    server.start();
    Thread notificationSender = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          for (int i = 0; i < 100; i++) {
            Thread.sleep(5000);
            final Transfer transfer = new Transfer();
            final Credit credit = new Credit("https://ledger.example/accounts/alice", "100", null);
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
        try {
          server.stop();
        } catch (MuleException e) {
          e.printStackTrace();
        }
      }
    }));
    System.in.read();
  }
}
