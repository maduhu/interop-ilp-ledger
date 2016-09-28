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
    final String sourceVale;
    final String targetValue;

    public PayloadContentMapper(final String sourceVale, final String targetValue ) {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("PayloadContentMapper");

        this.sourceVale = sourceVale;
        this.targetValue = targetValue;
    }

    /**
     * Message Transformer to replace all occurences of a source value in the payload with a target value
     *
     * @param muleMessage
     * @param s
     * @return
     * @throws TransformerException
     */
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
        String payload = (String)muleMessage.getPayload();

        return payload != null ? payload.replaceAll( sourceVale, targetValue ) : payload;
    }
}
