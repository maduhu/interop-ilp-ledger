package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ServiceUrlRegistryTest {

	ServiceUrlRegistry r = null;
	
	@Before
	public void setUp() throws Exception {
		r = new ServiceUrlRegistry();
	}

	@Test
	public void testConstructor() {
		r = new ServiceUrlRegistry();
		assertTrue(r != null);
	}
	
	
	@Test
	public void testGetHealth() {
		r.setHealth("I am very healthy");
		assertEquals("healthy", r.getHealth(), "I am very healthy");
	}
	
	
	@Test
	public void testAccount() {
		r.setAccount("account");
		assertEquals("testAccount", r.getAccount(), "account");
	}
	
	@Test
	public void testAccounts() {
		r.setAccounts("accounts");
		assertEquals("healthy", r.getAccounts(), "accounts");
	}
	
	@Test
	public void testTransfers() {
		r.setAccountTransfers("account transfers");
		assertEquals("testTransfers", r.getAccountTransfers(), "account transfers");
	}
	
	@Test
	public void testAuthToken() {
		r.setAuthToken("SuperSecretToken");
		assertEquals("testTransfers", r.getAuthToken(), "SuperSecretToken");
	}
	
	@Test
	public void testMessage() {
		r.setMessage("Message");
		assertEquals("testTransfers", r.getMessage(), "Message");
	}
	
	@Test
	public void testTransfer() {
		r.setTransfer("transfers");
		assertEquals("testTransfers", r.getTransfer(), "transfers");
	}
	
	@Test
	public void testAcctTransfer() {
		r.setTransferRejection("rejections");
		assertEquals("testAcctTransfer", r.getTransferRejection(), "rejections");
	}
	
	@Test
	public void testWebSocket() {
		r.setWebsocket("websocket");
		assertEquals("testTransfers", r.getWebsocket(), "websocket");
	}
	
	
	@Test
	public void testTransferFullfillment() {
		r.setTransferFulfillment("transfer fullfillment");
		assertEquals("testTransfers", r.getTransferFulfillment(), "transfer fullfillment");
	}
	
	
	@Test
	public void testTransferState() {
		r.setTransferState("transfer state");
		assertEquals("testTransfers", r.getTransferState(), "transfer state");
	}

}
