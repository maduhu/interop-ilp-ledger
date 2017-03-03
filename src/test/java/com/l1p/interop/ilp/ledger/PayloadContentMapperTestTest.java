package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayloadContentMapperTestTest {

	private static Connector connector = null;
	private static String CONNECTOR_NAME = "name of connector";
	private static String CONNECTOR_ID = "connector id";
	private static String CONNECTOR = "connector";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connector = new Connector();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testSetId() {
		connector.setId(CONNECTOR_ID);
		assertTrue("getid", connector.getId().equals(CONNECTOR_ID));
	}

	@Test
	public void testSetName() {
		connector.setName(CONNECTOR_NAME);
		assertTrue("test get connector", connector.getName().equals(CONNECTOR_NAME));
	}

	@Test
	public void testSetConnector() {
		connector.setConnector(CONNECTOR);
		assertTrue("get connector", connector.getConnector().equals(CONNECTOR));
	}

	

}
