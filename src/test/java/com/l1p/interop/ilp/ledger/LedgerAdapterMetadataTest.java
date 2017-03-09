package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LedgerAdapterMetadataTest {

	private LedgerAdapterMetadata a = null;
	
	@Before
	public void setUp() throws Exception {
		a = new LedgerAdapterMetadata();
	}

	@Test
	public void testCurrencyCode() {
		a.setCurrencyCode("USD");
		assertEquals("currencyCode", a.getCurrencyCode(), "USD");
	}
	
	
	@Test
	public void testCurrencySymbol() {
		a.setCurrencySymbol("$");
		assertEquals("currencySymbol", a.getCurrencySymbol(), "$");
	}
	
	
	@Test
	public void testPrecision() {
		a.setPrecision(82);
		assertEquals("precision", a.getPrecision(), 82);
	}
	
	
	@Test
	public void testScale() {
		a.setScale(95);
		assertEquals("scale", a.getScale(), 95);
	}
	
	
	@Test
	public void testConnectors() {
		
		List<Connector> connectors = new ArrayList<Connector>();
		Connector c = new Connector();
		c.setConnector("home");
		c.setId("1");
		c.setName("connector name");
		connectors.add(c);
		
		a.setConnectors(connectors);
		assertTrue("connector not null", a.getConnectors() != null);
		assertEquals("connector size", a.getConnectors().size(), 1);
	}
	
	
	@Test
	public void testUrls() {
		ServiceUrlRegistry r = new ServiceUrlRegistry();
		r.setAccount("account");
//		List<ServiceUrlRegistry> urls = new ArrayList<ServiceUrlRegistry>();
//		urls.add(r);
		a.setUrls(r);
		
		assertTrue("urls not null", a.getUrls() != null);
		assertEquals("urls size", a.getUrls().getAccount(), "account");
	}

}
