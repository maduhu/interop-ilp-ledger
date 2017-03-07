package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.assertEquals;


import org.apache.commons.codec.binary.Base64;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.component.SimpleCallableJavaComponentTestCase;


public class BasicAuthCredentialsTransformerTest extends SimpleCallableJavaComponentTestCase {

	/*
	 * As of 3/6/2017 
	 * 
	 * It appears that the BasicAuthCredentialsTransformer is not needed and this
	 * test class is may not be needed and the Mule code that calls the Basic Auth Transformer
	 * commented out.
	 * 
	 */
	
	@Test
	public void testTransform() throws Exception {
		
		final String payload = "OriginalPayload";
		final String username = "JoeSchmoe";
		final String password = "MonkeyBrainsPasswordThatNoOneWouldGuess";
		
		
		/* Prepare inbound message. */
		String authEncrypted = createEncryptedAuth(username, password);
		String authProperty = "Basic " + authEncrypted;
		
		MuleEvent event = getTestEvent(payload, muleContext);
		MuleMessage muleMessage = event.getMessage();
		
		muleMessage.setProperty("authorization", authProperty, PropertyScope.INBOUND);
        
        System.out.println("about to transform authorization");
        BasicAuthCredentialsTransformer transformer = new BasicAuthCredentialsTransformer();
        System.out.println("just returend from transformer");
        
        String response = (String) transformer.transformMessage(muleMessage, "UTF-8");
        System.out.println("transformed response : " + response);

        String basic_auth_user = muleMessage.getInvocationProperty("basic_auth_user");
        String basic_auth_password = muleMessage.getInvocationProperty("basic_auth_password");
        
        assertEquals("payload", response, payload);
        assertEquals("basic_auth_user", username, basic_auth_user);
        assertEquals("basic_auth_password", password, basic_auth_password);
	}
	
	
	private String createEncryptedAuth(String username, String password) {
		String stringToBeEncryped = username + ":" + password;
		return new String(Base64.encodeBase64(stringToBeEncryped.getBytes()));
	}
	

}
