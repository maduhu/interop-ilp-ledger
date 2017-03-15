package com.l1p.interop.ilp.ledger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;

import com.l1p.interop.ilp.ledger.notification.LedgerNotificationRegistrationApplication;
import com.l1p.interop.ilp.ledger.notification.LedgerNotificationRegistrationServer;

public class LedgerNotificationRegistrationServerTest {
	
	LedgerNotificationRegistrationServer server = null;
	LedgerNotificationRegistrationApplication app = null;
	

	@Before
	public void setUp() throws Exception {
	    app = new LedgerNotificationRegistrationApplication(new LedgerUrlMapper(".*/ledger/", "http://0.0.0.0/ledger/base/path", ".*/ledger/", "http://0.0.0.0/ledger/base/path"));

	    String staticContentDocRoot = null;
	    String property = "java.io.tmpdir";
	    staticContentDocRoot = System.getProperty(property);
	    
	    System.out.println("temp directory = " + staticContentDocRoot);
	    
	    int port = 10001;
	    String webSocketPath = "/websocket";
	    
//	     sample : "/tmp", 10001, "/websocket", app);
		server = new LedgerNotificationRegistrationServer(staticContentDocRoot, port, webSocketPath, app);
		
		assertTrue("server constructor created", server != null);
	}

	
	
	@Test
	public void testConstructor() {
		assertTrue("server constructor created", server != null);
	}
	
	
	@Test
	public void testInitialize() {
		try {
			server.initialise();
			assertEquals(1,1);
		} catch (InitialisationException e) {
			e.printStackTrace();
			fail("unable to start up notification server.  Reason = " + e.getLocalizedMessage());
		}
	}
	
	
	@Test()
	public void testStart() {
		try {
			
			if (server == null) 
				System.out.println("server is null in start");
			else
				System.out.println("server is not null");
			server.initialise();
			server.start();
		} catch (MuleException e) {
			e.printStackTrace();
			fail("unable to start up notification server.  Reason = " + e.getLocalizedMessage());
		}
	}
	
	
	@Test
	public void testStop() {
		try {
			server.initialise();
			server.stop();
			assertEquals(1,1);
		} catch (MuleException e) {
			e.printStackTrace();
			fail("unable to stop notification server.  Reason = " + e.getLocalizedMessage());
		}
	}
	
	
	@Test
	public void testDispose() {
		server.dispose();
		assertEquals(1,1);
	}

}
