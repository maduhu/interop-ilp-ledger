package com.l1p.interop.ilp.ledger.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class WebsocketClientEndpoint {

	private static final Logger log = LoggerFactory.getLogger(WebsocketClientEndpoint.class);

	final static CountDownLatch messageLatch = new CountDownLatch(1);

	private Session session;

	public WebsocketClientEndpoint() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			// String uri = "ws://localhost:8089/websocket?token=123";
			// String uri =
			// "ws://ec2-34-231-88-223.compute-1.amazonaws.com:8089/websocket?token=placeholder_9AtVZPN3t49Kx07stO813UHXv6pcES";
			String uri = "ws://ec2-34-206-201-170.compute-1.amazonaws.com:3002/websocket?token=dtNS8W1GPUPc_k-VXE7Llat0iSMf_TeI4sMm2Qoc7sL7LUdwk8CNOWuu2r48F_CwlxltHW5h02zh5GfXUJM5ZXdAf_uI_Td4Zxs";
			log.info("Connecting to {}", uri);
			container.connectToServer(this, URI.create(uri));
			//messageLatch.await(30, TimeUnit.MINUTES);
			log.info("Closing connection to {}", uri);
		} catch (DeploymentException | IOException ex) {
			log.error("Client experience exception", ex);
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connected to endpoint: " + session.getBasicRemote());
		try {
			// String subscriptionRequest = "{ \"jsonrpc\": \"2.0\", \"method\":
			// \"subscribe_account\", \"params\": { \"eventType\":\"*\",
			// \"accounts\": [
			// \"http://ec2-35-166-189-14.us-west-2.compute.amazonaws.com:8088/ilp/ledger/v1/accounts/dfsp1-connector-30mins\"
			// ] }, \"id\": 1 }";
			this.session = session;
		} catch (Exception ex) {
			log.error("error", ex);
		}
	}

	@OnMessage
	public void processMessage(String message) {
		log.info("Received message in client: " + message);
	}

	@OnClose
	public void processClose(CloseReason reason) {
		log.info("Closing the socket {}", reason.toString());
	}

	@OnError
	public void processError(Throwable t) {
		t.printStackTrace();
	}

	public void sendMessage() throws IOException {
		String subscriptionRequest = "{   \"jsonrpc\": \"2.0\",   \"method\": \"subscribe_account\",   \"params\": {  \"eventType\":\"*\",   \"accounts\": [       \"http://ec2-34-206-201-170.compute-1.amazonaws.com:3002/accounts/fsp2\"     ]   },   \"id\": 1 }";
		log.info("Sending message to endpoint: {}", subscriptionRequest);
		session.getBasicRemote().sendText(subscriptionRequest);
	}

	public static void main(String[] args) throws Exception {
		WebsocketClientEndpoint endPoint = new WebsocketClientEndpoint();
		endPoint.sendMessage();
		messageLatch.await(5, TimeUnit.MINUTES);
	}
}