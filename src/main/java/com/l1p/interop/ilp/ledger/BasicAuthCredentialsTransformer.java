package com.l1p.interop.ilp.ledger;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.Base64;

/**
 * Extracts and set the Basic Auth credentials to flow variables
 */
public class BasicAuthCredentialsTransformer extends AbstractMessageTransformer {

    /**
     * @param message
     * @param outputEncoding
     * @return
     * @throws TransformerException
     */
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        String basicAuth = message.getInboundProperty("authorization");
        if (basicAuth != null) {
            String decoded = new String(Base64.getDecoder().decode(basicAuth.substring(6)));
            String[] credentials = decoded.split(":", 2);
            if (credentials.length == 2) {
                message.setInvocationProperty("basic_auth_user", credentials[0]);
                message.setInvocationProperty("basic_auth_password", credentials[1]);
            }
        }
        return message.getPayload();
    }
}
