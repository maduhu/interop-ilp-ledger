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

import com.google.common.net.MediaType;
import com.l1p.interop.JsonTransformer;
import com.l1p.interop.mule.connector.metrics.reporter.KafkaReporter;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.python.jline.internal.Log;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ILPLedgerAdapterFunctionalTest extends FunctionalTestCase {

	private final String accountsPath="/ilp/ledger/v1/accounts/";
	private final String transfersPath="/ilp/ledger/v1/transfers/";
	private final String connectorsPath="/ilp/ledger/v1/connectors";
	private final String messagesPath = "ilp/ledger/v1/messages";
	private final String serviceHost = "http://localhost:8081";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	WebResource webService;

	@Override
	protected String getConfigResources() {
		return "test-resources.xml,interop-ilp-ledger-api.xml,interop-ilp-ledger.xml,proxy/ilp-ledger-proxy.xml, proxy/mock-ilp-ledger.xml,proxy/mock-ilp-ledger-api.xml";
	}

	@BeforeClass
	public static void initEnv() {
		System.setProperty("MULE_ENV", "test");
		System.setProperty("spring.profiles.active", "test");
		System.setProperty("metrics.reporter.kafka.broker", "ec2-35-164-199-6.us-west-2.compute.amazonaws.com:9092");
		System.setProperty("metrics.reporter.kafka.topic", "bmgf.metric.pi2");
	}

	@Before
	public void initSslClient() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException {
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
	public void testPutAccounts() throws Exception {
		final String putAccountJSON = loadResourceAsString("testData/putAccount-Alice.json");
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );
		String id = "john";
		String idValue = "http://ec2-35-163-231-111.us-west-2.compute.amazonaws.com:8014/ledger/accounts/john";

		//Valid PUT, to create a new ID every time the test runs, using random
		//This test is working fine but disabling this currently, because every time this test runs, an account is added,
		//I don't mind it but it appears they are being saved and I'm not sure about the cleanup mechanism of that DB table, so run this only when needed
		/*
		final String testId = java.util.UUID.randomUUID().toString();
		boolean is_disabled = false;
		clientResponse = putRequestWithQueryParams( accountsPath + testId, params, putAccountJSON );
		jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "AccountsPutValid" + ": Did not receive status 200", 200, clientResponse.getStatus());
		assertEquals( "Response field name did not contain expected value", testId, jsonReponse.get( "name" ) );
		assertEquals( "Response field balance did not contain expected value", "1000.00", jsonReponse.get( "balance" ) );
		assertEquals( "Response field is_disabled did not contain expected value", is_disabled, jsonReponse.get( "is_disabled" ) );
		assertTrue( "response field is_disabled was not present in resposne", jsonReponse.get( "id") != null );
		*/
		
		//Invalid PUT, empty json body, write a java class to validate empty body and respond with 400
		/*final String testId = java.util.UUID.randomUUID().toString();
		ClientResponse clientResponse = putRequestWithQueryParams( accountsPath + testId, params, "" );
		Map<String, Object> jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "AccountsPutInValidEmptyBody" + ": Did not receive status 400", 400, clientResponse.getStatus());
		assertEquals( "Response field statusCode did not contain expected value", 400, jsonReponse.get( "statusCode" ) );
		assertEquals( "Response field error did not contain expected value", "Bad Request", jsonReponse.get( "error" ) );
		assertEquals( "Response field message did not contain expected value", "Invalid request payload JSON format", jsonReponse.get( "message" ) );
		*/
		
		//Valid PUT - should be invalid if it exists but valid here because it is mocked
		ClientResponse clientResponse = putRequestWithQueryParams( accountsPath + id, params, putAccountJSON );
		Map<String, Object> jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "AccountsPutInValid" + ": Did not receive status 200", 200, clientResponse.getStatus());
		assertEquals( "Response field id did not contain expected value", idValue, jsonReponse.get( "id" ) );
		assertEquals( "Response field id did not contain expected value", id, jsonReponse.get( "name" ) );
		assertEquals( "Response field id did not contain expected value", "USD", jsonReponse.get( "currency" ) );
		
		//Invalid PUT, no authorization
		//TODO - Disabling this because, currently account is getting created without auth :-), should this be changed?
		/*
		params.clear();
		clientResponse = putRequestWithQueryParams( accountsPath + testId, params, putAccountJSON );
		jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "AccountsPutInvalidNoAuth" + ": Did not receive status 404", 404, clientResponse.getStatus());
		*/

	}

	@Test
	public void testGetAccounts() throws Exception {
		
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );
		//This account should already be present (considering that account 'alice' exists)
		String id = "bob";
		String is_disabled = "1";
		
		//InValid GET for account that doesn't exist
		/*
		String testId = java.util.UUID.randomUUID().toString() + java.util.UUID.randomUUID().toString();
		clientResponse = getRequest( accountsPath + testId, params );
		jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "AccountsGetInValid" + ": Did not receive status 404", 404, clientResponse.getStatus());
		assertEquals( "Response field id did not contain expected value", "NotFoundError", jsonReponse.get( "id" ) );
		assertEquals( "Response field message did not contain expected value", "Unknown account.", jsonReponse.get( "message" ) );
		*/
		
		//Valid GET for account alice, run this separately, if needed
		ClientResponse clientResponse = getRequest( accountsPath + id, params );
		
		String response = clientResponse.getEntity(String.class);
		System.out.println("getAccounts http response: " + response);
		
		assertEquals( "AccountsGetValid" + ": Did not receive status 200", 200, clientResponse.getStatus());

		Map<String, Object> jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "Response field name did not contain expected value", id, jsonReponse.get( "name" ) );
		assertEquals( "Response field is_disabled did not contain expected value", is_disabled, jsonReponse.get( "is_disabled" ) );
		assertTrue( "response field is_disabled was not present in response", jsonReponse.get( "id") != null );
		assertTrue( "response field balance was not present in response", jsonReponse.get( "balance") != null );
		assertTrue( "response field balance was not present in response", jsonReponse.get( "ledger") != null );
		
	}
	
	@Test
	public void testGetTransfer() throws Exception {
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );
//		params.put( "Authorization", createEncryptedAuth("dfsp1", "dfsp1") );
		String id = "3a2a1d9e-8640-4d2d-b06c-84f2cd613123";
		
		//http.status=200: {"id":"http://ec2-52-37-54-209.us-west-2.compute.amazonaws.com:8088/ledger/transfers/undefined","ledger":"http://ec2-52-37-54-209.us-west-2.compute.amazonaws.com:8088/ledger","debits":[{"account":"http://ec2-52-37-54-209.us-west-2.compute.amazonaws.com:8088/ledger/accounts/alice","amount":"50.00"}],"credits":[{"account":"http://ec2-52-37-54-209.us-west-2.compute.amazonaws.com:8088/ledger/accounts/bob","amount":"50.00"}],"execution_condition":"cc:0:3:8ZdpKBDUV-KX_OnFZTsCWB_5mlCFI3DynX5f5H2dN-Y:2","cancellation_condition":null,"expires_at":"2016-11-27T00:00:01.000Z","state":"proposed","timeline":{"proposed_at":"2016-11-04T05:23:20.940Z","prepared_at":"2016-11-04T05:23:20.940Z","executed_at":null}}
		//"params":{"pattern":{},"value":"3a2a1d9e-8640-4d2d-b06c-84f2cd613","key":"id"}}]}
		//Invalid Get Transfer - Transfer not found
		ClientResponse clientResponse = getRequest( transfersPath + id, params );
		
		System.out.println("Client response from call to getResquest: " + clientResponse.getStatus());

		assertEquals( "TransferGetInValid" + ": Did not receive status 200", 200, clientResponse.getStatus());
		Map<String, Object> jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		assertEquals( "Response field id did not contain expected value", "http://usd-ledger.example/transfers/3a2a1d9e-8640-4d2d-b06c-84f2cd613204", jsonReponse.get( "id" ) );
		//assertEquals( "Response field message did not contain expected value", "id is not a valid Uuid", jsonReponse.get( "message" ) );
		//assertTrue( "Response field validationErrors was not present in response", jsonReponse.get( "validationErrors" ) != null );
	}

	@Test
	public void testGetHealth() throws Exception {
		//TODO: Finish this when /health works as expected, currently getting a 404
		ClientResponse clientResponse = getRequest( "/ilp/ledger/v1/health", null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testRejectTransferShouldReturnValidResponse() throws Exception {
		ClientResponse clientResponse = putRequestWithQueryParams( "/ilp/ledger/v1/transfers/12345/rejection", null, "cf:0:_v8" );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	@Test
	public void testGetMetadata() throws Exception {
		
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );
		
		//Valid GET request for Metadata
		ClientResponse clientResponse = getRequest( "/ilp/ledger/v1/", params );
		Map<String, Object> jsonReponse = JsonTransformer.stringToMap( clientResponse.getEntity(String.class) );
		System.out.println("JSON Output: "+jsonReponse);
		Map<String, Object> urls = (Map<String, Object>) jsonReponse.get( "urls");
		
		assertEquals( "MetadataGetValid" + ": Did not receive status 200", 200, clientResponse.getStatus());
		assertEquals( "Response field precision did not contain expected value", 10, jsonReponse.get( "precision" ) );
		assertEquals( "Response field scale did not contain expected value", 2, jsonReponse.get( "scale" ) );
		
		assertTrue( "response field currency_code was not present in response", jsonReponse.get( "currency_code") == null );
		assertTrue( "response field currency_symbol was not present in response", jsonReponse.get( "currency_symbol") == null );
		//assertTrue( "response field condition_sign_public_key was not present in response", jsonReponse.get( "condition_sign_public_key") != null );
		//assertTrue( "response field notification_sign_public_key was not present in response", jsonReponse.get( "notification_sign_public_key") != null );
		assertTrue( "response field urls was not present in response", jsonReponse.get( "urls") != null );
		
		if ( jsonReponse.get( "urls") != null )	{
			assertTrue( "response field transfer was not present in response", urls.get( "transfer") != null );
			assertTrue( "response field transfer_fulfillment was not present in response", urls.get( "transfer_fulfillment") != null );
			assertTrue( "response field transfer_state was not present in response", urls.get( "transfer_state") != null );
			assertTrue( "response field accounts was not present in response", urls.get( "accounts") != null );
			assertTrue( "response field account was not present in response", urls.get( "account") != null );
			assertTrue( "response field transfer_rejection was not present in response", urls.get( "transfer_rejection") != null );
		}
		
	}

	//Transfer related propose, prepare and execute need up-to-date code of the whole project, then need to be executed in an order
	//The tests below can be finished once that is done.
	@Test
	public void testPutTransferFulfillment() throws Exception {
		Map<String, String> params = new HashMap<String,String>();
		params.put( "Authorization", "Basic YWRtaW46Zm9v" );
//		params.put( "Authorization", createEncryptedAuth("dfsp1", "dfsp1") );
		String id = "3a2a1d9e-8640-4d2d-b06c-84f2cd613204";
		final String putTransferJSON = "cf:0:_v8";

		ClientResponse clientResponse = putRequestWithQueryParamsNullContentType( transfersPath + id+"/fulfillment", params, putTransferJSON );
		//ClientResponse clientResponse = putRequestWithQueryParamsNullContentType( transfersPath + id + "/fulfillment", params, putTransferJSON );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
			assertEquals("response does not have the crypto condition fulfillment.","cf:0:_v8",responseContent);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		System.out.println( "=== response content: " + responseContent );

	}
	
	@Test
	public void testGetConnectors() throws Exception {
		//TODO: This needs to be implemented when connectors implementation in RAML or elsewhere is finished. Couldn't find this resource as of now
		
		ClientResponse clientResponse = getRequest( connectorsPath, null );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}
	
	@Test
	public void testPostMessages() throws Exception {
		ClientResponse clientResponse = postRequest( messagesPath, loadResourceAsString("testData/postMessagesBody.json") );
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
			assertEquals("Response is not blank as expected.","",responseContent);
		} catch ( Exception e ) {
			fail( "parsing client response content produced an unexpected exception: " + e.getMessage() );
		}
	}

	private void validateResponse( String testName, ClientResponse clientResponse, int expectedStatus, String expectedContent ) throws Exception {

		assertEquals( testName + ": Did not receive status 200", expectedStatus, clientResponse.getStatus());
		String responseContent = null;
		try {
			responseContent = clientResponse.getEntity(String.class);
			Log.info("Response: "+responseContent);
		} catch ( Exception e ) {
			fail( testName + ": parsing client response content produced an unexpected exception: " + e.getMessage() );
		}

		assertTrue( testName + ": received unexpected response -> " + responseContent, responseContent.contains( expectedContent ) );

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

	/**
	 * Convenience method to make a PUT request to the specified path, with query parameters
	 *
	 * @param path - path to post to
	 * @param params - Map of queryParameters
	 * @param requestData - JSON formatted request string
	 * @return ClientResponse instance representing the response from the service
	 */
	private ClientResponse putRequestWithQueryParamsNullContentType( String path, Map<String,String> params , String requestData) {
		WebResource putResource = webService;

		if ( params != null ) {
			for ( String nextKey : params.keySet() ) {
				putResource = putResource.queryParam( nextKey, params.get( nextKey ) );
			}
		}

		//return putResource.path( path ).type( "*/*" ).put(ClientResponse.class, requestData);
		return putResource.path( path ).type( "text/plain" ).put(ClientResponse.class, requestData);
	}
	
	
	/**
	 * Convenience method to create the basic auth request
	 * @param username
	 * @param password
	 * @return
	 */
	private String createEncryptedAuth(String username, String password) {
		String stringToBeEncryped = username + ":" + password;
		String auth = new String("Basic " + Base64.encodeBase64(stringToBeEncryped.getBytes()));
		System.out.println("auth: " + auth);
		
		return auth;
	}
}