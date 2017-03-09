package com.l1p.interop.ilp.ledger.notification;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.taskdefs.LoadResource;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.l1p.interop.ilp.ledger.LedgerUrlMapper;

import junit.framework.TestCase;

public class TestLedgerNotificationRegistrationApplication extends TestCase{

	@Test
	public void testApp() throws Exception {
		// TODO Auto-generated method stub
				LedgerNotificationRegistrationApplication app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper("test","test","test","test"));
				String json = FileUtils.readFileToString(new File("/Users/murthy/Clients/Gates/workspace/interop-ilp-ledger/src/test/resources/testData/rejectTransfer.json"));
				app.sendTranferExecutedNotification(json,"testFulfillment");
				
	}
	
	

}
