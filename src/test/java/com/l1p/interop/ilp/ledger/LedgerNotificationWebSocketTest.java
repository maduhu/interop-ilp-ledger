package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocketListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.l1p.interop.ilp.ledger.notification.LedgerNotificationWebSocket;

public class LedgerNotificationWebSocketTest {

	LedgerNotificationWebSocket ws = null;
	
	@Before
	public void setUp() throws Exception {
		
		// create needed mocks
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
				
		// example 
//		Mockito.verify(handler);
		
		ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
	}
	
	
	@Test
	public void testConstructor() {
		// create needed mocks
		ProtocolHandler handler = mock(ProtocolHandler.class);
		HttpRequestPacket requestPacket = mock(HttpRequestPacket.class);
		WebSocketListener listeners = mock(WebSocketListener.class);
		
		ws = new LedgerNotificationWebSocket(handler, requestPacket, listeners);
	}

	@Test
	public void test() {
		
		List<String> accounts = new ArrayList<String>();
		accounts.add("Account1");
		accounts.add("Account2");
		
		ws.setAccounts(accounts);
		
		assertEquals("get accounts back has proper count", ws.getAccounts().size(), 2);
	}

}
