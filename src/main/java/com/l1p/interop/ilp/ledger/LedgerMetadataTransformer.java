package com.l1p.interop.ilp.ledger;

import com.l1p.interop.JsonTransformer;
import com.l1p.interop.L1PErrorResponse;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;

import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class LedgerMetadataTransformer extends AbstractMessageTransformer {

    //private static final Logger logger = LogManager.getLogger(GetAccountTransformer.class);
    String ledgerURL = null;

    public LedgerMetadataTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("ServerMetadataTransformer");
    }

    public String getLedgerURL() {
        return ledgerURL;
    }

    public void setLedgerURL(String ledgerURL) {
        this.ledgerURL = ledgerURL;
    }

    /**
     * Message Transformer to augment metadata response returned by ledger to include paths to the ledger adapter for
     * supported functions
     *
     * @param muleMessage
     * @param s
     * @return
     * @throws TransformerException
     */
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
        Map<String,Object> jsonPayload = JsonTransformer.stringToMap( (String)muleMessage.getPayload() );

        Map<String,Object> urls = (Map<String,Object>)jsonPayload.get( "urls" );
        updateProperty( "health", ledgerURL + "/health", urls );
        updateProperty( "transfer", ledgerURL + "/transfers", urls );
        updateProperty( "transfer_fulfillment", ledgerURL + "/transfers/:id", urls );
        updateProperty( "transfer_state", ledgerURL + "/transfers/:id/state", urls );
        updateProperty( "accounts", ledgerURL + "/accounts", urls );
        updateProperty( "account", ledgerURL + "/accounts/:name", urls );

        return JsonTransformer.mapToString( jsonPayload );
    }

    private void updateProperty( String key, String value, Map<String,Object> target ) {
        if ( target.containsKey( key ) ) {
            target.put( key, value );
        }
    }
}
