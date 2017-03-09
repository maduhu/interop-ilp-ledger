package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.*;

import org.codehaus.jettison.json.JSONException;
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


	@Test
	public void testConstructor() {
		String ledgerAdapterURL = "someUrl";
		String ledgerAccountTransfersURL = "someOtherUrl";

		// Exercise the constructor
		LedgerMetadataTransformer transformer = new LedgerMetadataTransformer(ledgerAdapterURL, ledgerAccountTransfersURL);
		
		// verify that the constructor set the local properties correctly
		assertEquals("ledgerAdapterURL matches", ledgerAdapterURL, transformer.ledgerAdapterURL);
		assertEquals("ledgerAccountTransfersURL matches", ledgerAccountTransfersURL, transformer.ledgerAccountTransfersURL);
	}
	
	
	@Test
	public void testTransform() throws Exception {
		/* Prepare inbound message. */
		String payload = buildTransformerData();
		
		String ledgerAdapterURL = "someLedgerUrl";
		String ledgerAccountTransfersURL = "someOtherUrl";

		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
        
        System.out.println("about to transform Ledger Metadata");
        LedgerMetadataTransformer transformer = new LedgerMetadataTransformer(ledgerAdapterURL, ledgerAccountTransfersURL );
        System.out.println("just returend from transformer");
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
        System.out.println("transformed response : " + response);

        JSONObject jObject  = new JSONObject(response); // json
        JSONObject data = jObject.getJSONObject("urls"); // get data object
        
        String healthUrl = (String) data.get("health");
        System.out.println("Health URL after transform: " + healthUrl);
        
        assertEquals("health", data.getString("health"), ledgerAdapterURL+"/health");
        assertEquals("transfer", data.getString("transfer"), ledgerAdapterURL+"/transfers/:id");
        assertEquals("transfer_fulfillment", data.getString("transfer_fulfillment"), ledgerAdapterURL+"/transfers/:id/fulfillment");
        assertEquals("transfer_state", data.getString("transfer_state"), ledgerAdapterURL+"/transfers/:id/state");
        assertEquals("accounts", data.getString("accounts"), ledgerAdapterURL+"/accounts");
        assertEquals("account", data.getString("account"), ledgerAdapterURL+"/accounts/:name");
        
        assertEquals("auth_token", data.getString("auth_token"), ledgerAdapterURL+"/auth_token");
        assertEquals("message", data.getString("message"), ledgerAdapterURL+"/messages");
        assertEquals("websocket", data.getString("websocket"), "ws://localhost:8089/websocket");
        
	}
	
	
	private String buildTransformerData() throws JSONException {
		JSONObject json = new JSONObject();
		JSONObject urlsObject = new JSONObject();
		
		urlsObject.put("health", "health_url");
		urlsObject.put("transfer", "transfer_url");
		urlsObject.put("transfer_fulfillment", "transfer_Fullfillment_url");
		urlsObject.put("transfer_state", "transfer_state_url");
		urlsObject.put("accounts", "accounts_url");
		urlsObject.put("account", "account_url");
		
		
		
		json.put("urls", urlsObject);
		System.out.println("json object :: " + json.toString() );
		
		return json.toString();
		
	}

}
