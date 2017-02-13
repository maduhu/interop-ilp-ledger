package com.l1p.interop.ilp.ledger.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class WebsocketClientEndpoint {
  private static final Logger log =  LoggerFactory.getLogger(WebsocketClientEndpoint.class);

  @OnOpen
  public void onOpen(Session session) {
    System.out.println("Connected to endpoint: " + session.getBasicRemote());
    try {
      String subscriptionRequest = "{   \"jsonrpc\": \"2.0\",   \"method\": \"subscribe_account\",   \"params\": {     \"accounts\": [       \"http://ec2-35-163-231-111.us-west-2.compute.amazonaws.com:8081/ilp/ledger/v1/accounts/merchant\"     ]   },   \"id\": 1 }";
      log.info("Sending message to endpoint: {}", subscriptionRequest);
      session.getBasicRemote().sendText(subscriptionRequest);
    } catch (IOException ex) {
      log.error("error", ex);
    }
  }

  @OnMessage
  public void processMessage(String message) {
    log.info("Received message in client: " + message);
  }

  @OnError
  public void processError(Throwable t) {
    t.printStackTrace();
  }
}