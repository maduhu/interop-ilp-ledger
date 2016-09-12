package com.l1p.interop.ilp.ledger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bryan on 8/17/2016.
 */
public class SampleProcessor implements Callable {
    private static final Logger logger = LogManager.getLogger(SampleProcessor.class);

    public SampleProcessor() {
    }


    public Object onCall(MuleEventContext muleEventContext) throws Exception {
		return null;

    }
}
