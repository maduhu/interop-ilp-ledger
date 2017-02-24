package com.l1p.interop.ilp.ledger.notification;


import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NotificationClient {
  final static CountDownLatch messageLatch = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      String uri = "ws://localhost:8089/websocket?token=123";
      //String uri = "ws://ec2-35-166-189-14.us-west-2.compute.amazonaws.com:8089/websocket?token=placeholder_9AtVZPN3t49Kx07stO813UHXv6pcES";
      System.out.println("Connecting to " + uri);
      container.connectToServer(WebsocketClientEndpoint.class, URI.create(uri));
      messageLatch.await(300, TimeUnit.SECONDS);
    } catch (DeploymentException | InterruptedException | IOException ex) {
      ex.printStackTrace();
    }
  }

}