package com.l1p.interop.ilp.ledger.notification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
//import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NotificationClient {
  private static final Logger log =  LoggerFactory.getLogger(NotificationClient.class);
  final static CountDownLatch messageLatch = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//      String uri = "ws://localhost:8089/websocket?token=123";
      //String uri = "ws://ec2-34-231-88-223.compute-1.amazonaws.com:8089/websocket?token=placeholder_9AtVZPN3t49Kx07stO813UHXv6pcES";
      String uri = "ws://ec2-34-206-201-170.compute-1.amazonaws.com:3002/websocket?token=dtNS8W1GPUPc_k-VXE7Llat0iSMf_TeI4sMm2Qoc7sL7LUdwk8CNOWuu2r48F_CwlxltHW5h02zh5GfXUJM5ZXdAf_uI_Td4Zxs";
      log.info("Connecting to {}", uri);
      container.connectToServer(WebsocketClientEndpoint.class, URI.create(uri));
      messageLatch.await(30, TimeUnit.MINUTES);
      log.info("Closing connection to {}", uri);
    } catch (DeploymentException | InterruptedException | IOException ex) {
      log.error("Client experience exception", ex);
    }
  }

}
