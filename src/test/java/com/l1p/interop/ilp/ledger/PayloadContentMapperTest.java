package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.component.SimpleCallableJavaComponentTestCase;


public class PayloadContentMapperTest extends SimpleCallableJavaComponentTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testTheConstructor() throws Exception {
		MuleEvent event = getTestEvent("payload", muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        // Testing the constructor.
        PayloadContentMapper transformer = new PayloadContentMapper("xyz", "abc" );
	}
	
	
	@Test
	public void testMessageTransformerWithPayloadWithReplace() throws Exception {
		
		Object payload = "Pock";
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("P", "R" );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("payload after the transformer: " + response);
        assertEquals("testMessageTransformerWithPayloadWithReplace", "Rock", response);
	}
	
	
	@Test
	public void testMessageTransformerWithPayloadNoReplace() throws Exception {
		
		Object payload = "Mississippi";
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("x", "AAAA" );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("payload after the transformer: " + response);
        assertEquals("testMessageTransformerWithPayloadNoReplace", "Mississippi", response);
	}
	
	
	@Test(expected=Exception.class)
	public void testMessageTransformerWithPayloadNoTarget() throws Exception {
		
		Object payload = "Mississippi";
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("Miss", null );
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("payload after the transformer: " + response);
        assertEquals("testMessageTransformerWithPayloadNoTarget", "Mississippi", response);
	}
	
	
	@Test
	public void testMessageTransformerWithEmptyStringPayload() throws Exception {
		
		Object payload = "";
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("P", "R" );
        Object response = transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("payload after the transformer: " + response);
        assertTrue("testMessageTransformerWithEmptyStringPayload", response.equals(""));
		
	}
	
	
	@Test
	public void testMessageTransformerWithNullPayload() throws Exception {
		
		Object payload = null;
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("P", "R" );
        Object response = transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("response = " + response.getClass());
        
        System.out.println("payload after the transformer: " + response);
        assertTrue("testMessageTransformerWithNullPayload", response.getClass() == org.mule.transport.NullPayload.class);
		
	}
	
	
	@Test
	public void testMessageTransformerWithNullStringPayload() throws Exception {
		
		Object payload = new String();
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
        
        PayloadContentMapper transformer = new PayloadContentMapper("P", "R" );
        Object response = transformer.transformMessage(muleMessage, "UTF-8");
        
        System.out.println("response = " + response.getClass());
        
        System.out.println("payload after the transformer: " + response);
        assertTrue("testMessageTransformerWithNullStringPayload", response.equals(""));
		
	}

}
