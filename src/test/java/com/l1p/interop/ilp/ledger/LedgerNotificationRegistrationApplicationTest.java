package com.l1p.interop.ilp.ledger;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.taskdefs.LoadResource;
import org.glassfish.grizzly.websockets.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.tck.junit4.FunctionalTestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.glassfish.grizzly.http.HttpRequestPacket;

import com.l1p.interop.ilp.ledger.LedgerUrlMapper;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationRegistrationApplication;

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
		
		// example 
		Mockito.verify(handler);

		
		// perform the test
		WebSocket webSocket = app.createSocket(handler, requestPacket, listeners);
		
		assertTrue("did we create a websocket", webSocket != null);
	}
	
	
	@Test
	public void testOnConnect() {
		// create needed mocks
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		WebSocket webSocket = app.createSocket(handler, requestPacket, listeners);
//		webSocket.
		
		// this is the actual test case we are performing.
		app.onConnect(webSocket);
		
	}
	
	
	
	

//	@Test
//	public void testApp() throws Exception {
//		app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper("test","test","test","test"));
//		String json = FileUtils.readFileToString(new File("src/test/resources/testData/rejectTransfer.json"));
//		app.sendTranferExecutedNotification(json);
//	}
//	
	

}
