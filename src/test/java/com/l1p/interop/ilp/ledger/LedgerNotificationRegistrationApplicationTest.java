package com.l1p.interop.ilp.ledger;

import static org.mockito.Mockito.mock;
//import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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
import com.l1p.interop.ilp.ledger.domain.Credit;
import com.l1p.interop.ilp.ledger.domain.Debit;
import com.l1p.interop.ilp.ledger.domain.Message;
import com.l1p.interop.ilp.ledger.domain.Timeline;
import com.l1p.interop.ilp.ledger.domain.Transfer;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationRegistrationApplication;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationWebSocket;

//import junit.framework.Assert;
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
	
	
	@Test(expected=RuntimeException.class)
	public void testOnMessage() {
		String message = "{\"id\":24234,\"jsonrpc\":\"2.0\",\"method\":\"connect\",\"params\":{\"accounts\":[\"Apple\",\"Facebook\"],\"eventType\":\"Connect2017\"}}";
		
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		DataFrame frame = mock(DataFrame.class);
		LedgerNotificationWebSocket ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
		
		try {
			app.onMessage(ws, message);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("localized message: " + e.getLocalizedMessage() );
			assertEquals(e.getLocalizedMessage(), "Socket is not connected.");
		}
		
	}
	
	
	@Test(expected=RuntimeException.class)
	public void testOnMessageWithMoc() {
		String message = "{\"id\":24234,\"jsonrpc\":\"2.0\",\"method\":\"connect\",\"params\":{\"accounts\":[\"Apple\",\"Facebook\"],\"eventType\":\"Connect2017\"}}";
		LedgerNotificationWebSocket ws2 = mock(LedgerNotificationWebSocket.class);
		
		
		// this is the actual test case we are performing.
		app.onMessage(ws2, message);		
	}
	
	
	@Test
	public void testOnClose() {
		
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		DataFrame frame = mock(DataFrame.class);
		LedgerNotificationWebSocket ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
				
		List<String> accounts = new ArrayList<String>();
		accounts.add("test");
		
		ws.setAccounts(accounts);
		
		// setting up subscriptions so onClose can work.
        ConcurrentHashMap<String, Set<WebSocket>> subscriptions = new ConcurrentHashMap<String, Set<WebSocket>>();
        
        Set<WebSocket> newWebSockets = new CopyOnWriteArraySet<WebSocket>();
        newWebSockets.add(ws);
        
        Set<WebSocket> newWebSockets2 = new CopyOnWriteArraySet<WebSocket>();
        newWebSockets.add(ws);
        
        subscriptions.put("test", newWebSockets);
        
        app.setSubscriptions(subscriptions);
		

		// this is the actual test case we are performing.
		app.onClose(ws, frame);
	}
	
	
	/*
	 * This method 
	 */
	@Test
	public void testSendMessageNotification() throws JsonProcessingException {
		
		Message msg = new Message();
		//msg.setData("this is the data object");
		msg.setFrom("Tester1");
		msg.setLedger("ledger name");
		msg.setTo("Sender1");
		
		ObjectMapper om = new ObjectMapper();
		//String jsonMsg = om.writeValueAsString(msg);
		String jsonMsg = "{\"id\":null,\"ilp\":null,\"ledger\":\"ledger name\",\"from\":\"Tester1\",\"to\":\"Sender1\"}";
		
		// this is the actual test case we are performing.
		app.sendMessageNotification(jsonMsg);
	}
	
	
	@Test
	public void testSendTransferPreparedNotification() throws IOException {
		String json = FileUtils.readFileToString(new File("src/test/resources/testData/proposeTransferBody.json"));
		app.sendTransferPreparedNotification(json);
	}
	
	
	@Test
	public void testSendTransferNotificationPositive() {
		
		try {
			Transfer t = new Transfer();
			t.setAdditionalInfo("additional info");
			t.setCancellationCondition(null);
			
			List<Credit> credits = new ArrayList<Credit>();
			Credit credit = new Credit();
			credit.setAccount("testAccount1");
			credit.setAmount("2342.32");
			credit.setMemo(null);
			credits.add(credit);
			t.setCredits(credits);
			
			List<Debit> debits = new ArrayList<Debit>();
			Debit debit = new Debit();
			debit.setAccount("debitAccount2");
			debit.setAmount("2342.32");
			debit.setAuthorized(true);
			debit.setMemo(null);
			t.setDebits(debits);
			
			
			t.setExecutionCondition("some condition");
			t.setExpiresAt(new Date());
			t.setId("2234234");
			t.setLedger("someledger");
			t.setRejectionReason(null);
			t.setState("prepared");
			
			Timeline tl = new Timeline();
			tl.setExecutedAt(new Date());
			tl.setPreparedAt(new Date());
			tl.setRejectedAt(new Date());
			t.setTimeline(tl);
			
			ObjectMapper om = new ObjectMapper();
			String jsonMsg = om.writeValueAsString(t);
			
			app.sendTranferExecutedNotification(jsonMsg, "not sure what this should be");
		} catch (JsonProcessingException e) {
			fail("testSendTransferNotification failed on the following exception: " + e.getLocalizedMessage());
		}
	}
		
		
	@Test(expected=RuntimeException.class)
	public void testSendTransferNotificationException() {
		try {
			app.sendTranferExecutedNotification("{ \"message\" : \"this is a message that should error\" } ", "not sure what this should be");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Failed to convert to Transfer");
		}
	}
	
	
	@Test
	public void testTransferRejectedNotification() {
		
		try {
			Transfer t = new Transfer();
			t.setAdditionalInfo("additional info");
			t.setCancellationCondition(null);
			
			List<Credit> credits = new ArrayList<Credit>();
			Credit credit = new Credit();
			credit.setAccount("testAccount1");
			credit.setAmount("2342.32");
			credit.setMemo(null);
			credits.add(credit);
			t.setCredits(credits);
			
			List<Debit> debits = new ArrayList<Debit>();
			Debit debit = new Debit();
			debit.setAccount("debitAccount2");
			debit.setAmount("2342.32");
			debit.setAuthorized(true);
			debit.setMemo(null);
			t.setDebits(debits);
			
			
			t.setExecutionCondition("some condition");
			t.setExpiresAt(new Date());
			t.setId("2234234");
			t.setLedger("someledger");
			t.setRejectionReason("cancelled transaction");
			t.setState("prepared");
			
			Timeline tl = new Timeline();
			tl.setExecutedAt(null);
			tl.setPreparedAt(null);
			tl.setRejectedAt(new Date());
			t.setTimeline(tl);
			
			ObjectMapper om = new ObjectMapper();
			String jsonMsg = om.writeValueAsString(t);
			
			app.sendTranferRejectedNotification(jsonMsg);
		} catch (JsonProcessingException e) {
			assertEquals(e.getMessage(), "Failed to convert to Transfer");
		}
	}

	
	@Test
	public void testOnError() {
		
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		DataFrame frame = mock(DataFrame.class);
		LedgerNotificationWebSocket ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
		
		LedgerNotificationRegistrationApplication service = new LedgerNotificationRegistrationApplication(null);
        Class clazz = service.getClass();
        
        Throwable th = new Throwable("here is an error message");
        
        boolean response = app.onErrorForTesting(ws, th);
        org.junit.Assert.assertEquals("response from onMessage", true, response);
        
        
//		try {
//			Method method = clazz.getDeclaredMethod("onError", WebSocket.class, Throwable.class);
//			method.setAccessible(true);
//			Boolean response = (Boolean) method.invoke(clazz, ws, th);
//			
//			org.junit.Assert.assertEquals("response from onMessage", true, response);
//		} catch (NoSuchMethodException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			//  Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	

//	@Test
//	public void testApp() throws Exception {
//		app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper("test","test","test","test"));
//		String json = FileUtils.readFileToString(new File("src/test/resources/testData/rejectTransfer.json"));
//		app.sendTranferExecutedNotification(json);
//	}
//	
	

}
