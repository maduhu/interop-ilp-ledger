package com.l1p.interop.ilp.ledger;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ILPLedgerAdapterFunctionalTest extends FunctionalTestCase {

	private final String accountsPath="/ilp/ledger/v1/accounts";
	private final String transfersPath="/ilp/ledger/v1/transfers";
	private final String connectorsPath="/ilp/ledger/v1/connectors";
	private final String serviceHost = "http://localhost:8081";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	WebResource webService;

	@Override
	protected String getConfigResources() {
		return "test-resources.xml,interop-ilp-ledger-api.xml,interop-ilp-ledger.xml";
	}

	@BeforeClass
	public static void initEnv() {
		System.setProperty("MULE_ENV", "test");
		System.setProperty("spring.profiles.active", "test");
	}

	@Before
	public void initSslClient() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
			KeyManagementException {
		ClientConfig config = new DefaultClientConfig();
		webService = Client.create(config).resource(serviceHost);
	}

	@Test
	public void testInvalidPathShouldReturn404() throws Exception {
		final String invalidPath = "path/shouldnt/exist";
		final String notJSON = "<BadRequest>This is not JSON</BadRequest>";
		logger.info("Posting event to web services");

		ClientResponse clientResponse = postRequest(invalidPath, notJSON);
		validateResponse( "InvalidPathShouldReturn404", clientResponse, 404, "Resource not found");
	}

	@Test
	public void testValidPutAccountShouldReturnValidResponse() throws Exception {
		final String putAccountJSON = loadResourceAsString("testData/putAccount-Alice.json");
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );

		ClientResponse clientResponse = putRequestWithQueryParams( accountsPath + "/alice", params, putAccountJSON );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		System.out.println( "=== response content: " + responseContent );

	}

	@Test
	public void testValidGetAccountsShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( accountsPath, null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testValidGetHealthShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( "/ilp/ledger/v1/health", null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testValidGetMetadataShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( "/ilp/ledger/v1/", null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testValidGetAccountShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( accountsPath + "/bryan", null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testValidGetTransferShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( transfersPath + "/3a2a1d9e-8640-4d2d-b06c-84f2cd613204", null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testValidGetConnectorsShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = getRequest( connectorsPath, null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	private void validateResponse( String testName, ClientResponse clientResponse, int expectedStatus, String expectedContent ) throws Exception {

		assertEquals( testName + ": Did not receive status 200", expectedStatus, clientResponse.getStatus());
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( testName + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		assertTrue( testName + ": received unexpected response", responseContent.contains( expectedContent ) );

	}

	/**
	 * Convenience method to post a request to the specified path.
	 *
	 * @param path - path to post to
	 * @param params - Map of queryParams
	 * @return ClientResponse instance representing the response from the service
	 */
	private ClientResponse getRequest( String path, Map<String,String> params ) {
		WebResource getResource = webService;

		if ( params != null ) {
			for ( String nextKey : params.keySet() ) {
				getResource = getResource.queryParam( nextKey, params.get( nextKey ) );
			}
		}

		return getResource.path( path ).type( "application/json").get(ClientResponse.class);
	}
	/**
	 * Convenience method to post a request to the specified path.
	 *
	 * @param path - path to post to
	 * @param requestData - JSON formatted request string
     * @return ClientResponse instance representing the response from the service
     */
	private ClientResponse postRequest( String path, String requestData ) {
		return webService.path( path ).type( "application/json").post(ClientResponse.class, requestData);
	}

	/**
	 * Convenience method to make a PUT request to the specified path, with query parameters
	 *
	 * @param path - path to post to
	 * @param params - Map of queryParameters
	 * @param requestData - JSON formatted request string
	 * @return ClientResponse instance representing the response from the service
	 */
	private ClientResponse putRequestWithQueryParams( String path, Map<String,String> params , String requestData) {
		WebResource putResource = webService;

		if ( params != null ) {
			for ( String nextKey : params.keySet() ) {
				putResource = putResource.queryParam( nextKey, params.get( nextKey ) );
			}
		}

		return putResource.path( path ).type( "application/json").put(ClientResponse.class, requestData);
	}
}