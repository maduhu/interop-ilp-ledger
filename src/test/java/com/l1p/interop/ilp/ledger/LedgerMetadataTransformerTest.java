package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.component.SimpleCallableJavaComponentTestCase;


public class LedgerMetadataTransformerTest extends SimpleCallableJavaComponentTestCase {

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
	public void test() {
		fail("Not yet implemented");
		
		/* Prepare inbound message. */
//		Object payload = buildTransformerData("userURI", "userURI");
//		MuleEvent event = getTestEvent(payload, muleContext);
//		MuleMessage muleMessage = event.getMessage();
//        muleMessage.setProperty("id", "some name", PropertyScope.SESSION);
//        
//        GetAccountTransformer transformer = new GetAccountTransformer( createStoreData("userURI") );
//        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
//
//        JSONObject jObject  = new JSONObject(response); // json
//        JSONObject data = jObject.getJSONObject("result"); // get data object
//        
//        assertEquals("name of account", data.getString("name"));
//        assertEquals("USD", data.getString("currency"));
//        assertEquals("some account name", data.getString("account"));
	}

}
