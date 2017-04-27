package com.l1p.interop.ilp.ledger;

import com.l1p.interop.JsonTransformer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;

import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class LedgerMetadataTransformer extends AbstractMessageTransformer {

    //private static final Logger logger = LogManager.getLogger(GetAccountTransformer.class);
    final String ledgerAdapterURL;
    final String ledgerAccountTransfersURL;

    public LedgerMetadataTransformer( final String ledgerAdapterURL, final String ledgerAccountTransfersURL ) {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("ServerMetadataTransformer");
        this.ledgerAdapterURL = ledgerAdapterURL;
        this.ledgerAccountTransfersURL = ledgerAccountTransfersURL;
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
        updateProperty( "health", ledgerAdapterURL + "/health", urls );
        updateProperty( "transfer", ledgerAdapterURL + "/transfers/:id", urls );
        updateProperty( "transfer_fulfillment", ledgerAdapterURL + "/transfers/:id/fulfillment", urls );
        updateProperty( "transfer_state", ledgerAdapterURL + "/transfers/:id/state", urls );
        updateProperty( "accounts", ledgerAdapterURL + "/accounts", urls );
        updateProperty( "account", ledgerAdapterURL + "/accounts/:name", urls );
        urls.put( "transfer_rejection", ledgerAdapterURL + "/transfers/:id/rejection" );

        //urls.put("account_transfers", ledgerAccountTransfersURL);
        urls.put("auth_token", ledgerAdapterURL+"/auth_token");
        urls.put("message", ledgerAdapterURL+"/messages");
        urls.put("websocket", "ws://localhost:8089/websocket");

        return JsonTransformer.mapToString( jsonPayload );
    }

    private void updateProperty( String key, String value, Map<String,Object> target ) {
        if ( target.containsKey( key ) ) {
            target.put( key, value );
        }
    }
}
