package com.l1p.interop.ilp.ledger;

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
public class SampleTransformer extends AbstractMessageTransformer {

    //private static final Logger logger = LogManager.getLogger(GetAccountTransformer.class);

    public SampleTransformer() {
        registerSourceType(DataTypeFactory.create(Map.class));
        setReturnDataType((DataTypeFactory.create(String.class)));
        setName("SampleTransformer");
    }

    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
		return null;
    }
}
