package com.l1p.interop.ilp.ledger;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;

import java.util.Map;

/**
 * Created by Bryan on 9/28/2016.
 */
public class PayloadContentMapper extends AbstractMessageTransformer {

    //private static final Logger logger = LogManager.getLogger(PayloadContentMapper.class);
    final String sourceValue;
    final String targetValue;

    public PayloadContentMapper(final String sourceValue, final String targetValue ) {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("PayloadContentMapper");

        this.sourceValue = sourceValue;
        this.targetValue = targetValue;
    }

    /**
     * Message Transformer to replace all occurrences of a source value in the payload with a target value
     *
     * @param muleMessage
     * @param s
     * @return
     * @throws TransformerException
     */
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
//        String payload = (String)muleMessage.getPayload();
        Object oPayload = muleMessage.getPayload();
        String payload = null;
        
        if (oPayload instanceof String) {
        	payload = (String) oPayload;
        	return payload != null && payload.length()>0 ? payload.replaceAll( sourceValue, targetValue ) : payload;
        } else {
        	return oPayload;
        }

    }
}
