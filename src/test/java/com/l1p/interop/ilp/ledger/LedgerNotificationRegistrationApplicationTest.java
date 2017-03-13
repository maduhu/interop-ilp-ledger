package com.l1p.interop.ilp.ledger;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1p.interop.ilp.ledger.domain.Message;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationRegistrationApplication;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationWebSocket;

import junit.framework.TestCase;

public class LedgerNotificationRegistrationApplicationTest extends TestCase{
	
	private LedgerNotificationRegistrationApplication app = null;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()");
		app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper("test","test","test","test"));
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown()");
		app = null;
	}
	
	
	@Test
	public void testLoadingResourceFile() throws IOException {
		String json = FileUtils.readFileToString(new File("src/test/resources/testData/rejectTransfer.json"));
		assertTrue("test loading a resource file", json != null);
		assertTrue("test loading a resource file", json.length() > 0);
	}
	
	
	@Test
	public void testCreateSocket() {

		// create needed mocks
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		// perform the test
		WebSocket webSocket = app.createSocket(handler, requestPacket, listeners);
		
		assertTrue("we create a websocket", webSocket != null);
	}
	
	
	@Test
	public void testOnConnect() {
		// create needed mocks
		WebSocket webSocket2 = mock(WebSocket.class);
		
		// this is the actual test case we are performing.
		app.onConnect(webSocket2);

		
	}
	
	
	@Test
	public void testOnMessage() {
		WebSocket webSocket2 = mock(WebSocket.class);
		
		// this is the actual test case we are performing.
		app.onMessage(webSocket2, "{ \"jsonrpc\": \"2.0\", \"method\": \"connect\", \"id\": \"234\"}");
		
	}
	
	
	@Test
	public void testOnClose() {
		
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		DataFrame frame = mock(DataFrame.class);
//		WebSocket webSocket2 = mock(WebSocket.class);
//		LedgerNotificationWebSocket ws = mock(LedgerNotificationWebSocket.class);
		LedgerNotificationWebSocket ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
				
		List<String> accounts = new ArrayList<String>();
		accounts.add("test");
		accounts.add("test2");
		
		ws.setAccounts(accounts);
		
		// this is the actual test case we are performing.
		app.onClose(ws, frame);
	}
	
	
	/*
	 * This method 
	 */
	@Test
	public void testSendMessageNotification() throws JsonProcessingException {
		
		Message msg = new Message();
		msg.setData("this is the data object");
		msg.setFrom("Tester1");
		msg.setLedger("ledger name");
		msg.setTo("Sender1");
		
		ObjectMapper om = new ObjectMapper();
		String jsonMsg = om.writeValueAsString(msg);
		
		// this is the actual test case we are performing.
		app.sendMessageNotification(jsonMsg);
	}
	
	
	@Test
	public void testSendTransferPreparedNotification() throws IOException {
		
		String json = FileUtils.readFileToString(new File("src/test/resources/testData/proposeTransfer.json"));
		app.sendTransferPreparedNotification(json);
		
		
	}
	
	
	

//	@Test
//	public void testApp() throws Exception {
//		app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper("test","test","test","test"));
//		String json = FileUtils.readFileToString(new File("src/test/resources/testData/rejectTransfer.json"));
//		app.sendTranferExecutedNotification(json);
//	}
//	
	

}
