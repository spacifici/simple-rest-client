/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package it.pacs.rest.test;

import it.pacs.rest.annotatios.GET;
import it.pacs.rest.annotatios.Path;
import it.pacs.rest.annotatios.PathParam;
import it.pacs.rest.annotatios.QueryParam;
import it.pacs.rest.annotatios.RestService;
import it.pacs.rest.factories.RestClientFactory;
import it.pacs.rest.interfaces.RestClientInterface;
import it.pacs.rest.test.support.TestServer;
import junit.framework.TestCase;

/**
 * @author Stefano Pacifici
 *
 */
public class RestClientFactoryTest extends TestCase {

	final static String localIP="http://127.0.0.1:8080";
	final static String localHost="http://localhost:8080/";
	
	@RestService(localHost)
	interface TestService {
		@GET
		@Path("/strings/{pp1}/code/{pp2}")
		String getString( @PathParam("pp1") String pathParam1,
				@PathParam("pp2") int pathParam2,
				@QueryParam("qp1") int queryParam1,
				@QueryParam("qp2") String queryParam2);
	}
	
	private TestServer testServer = new TestServer();
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testServer.start();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		testServer.stop();
	}
	
	public void testRestClientInterface() {
		TestService service = RestClientFactory.createClient(TestService.class);
		assertNotNull(service);
		RestClientInterface interf = (RestClientInterface) service;
		String baseUrl = interf.getBaseUrl();
		assertNotNull(baseUrl);
		assertTrue(localHost.equals(baseUrl));
		interf.setBaseUrl(localIP);
		assertTrue(localIP.equals(interf.getBaseUrl()));
	}
	
	public void testTestServiceInterface() {
		TestService service = RestClientFactory.createClient(TestService.class);
		assertNotNull(service);
		String result = service.getString("Test1", 13, 14, "Test1");
		assertNotNull(result);
	}

}
