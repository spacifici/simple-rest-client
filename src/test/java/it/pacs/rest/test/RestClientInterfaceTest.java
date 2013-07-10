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

import it.pacs.rest.factories.RestClientFactory;
import it.pacs.rest.interfaces.RestClientInterface;
import it.pacs.rest.test.support.TestServer;
import it.pacs.rest.test.support.TestService;
import junit.framework.TestCase;

/**
 * Test {@link RestClientInterface}
 *
 * @author Stefano Pacifici
 */
public class RestClientInterfaceTest extends TestCase {

    private static final String localHost = "http://localhost:9080";
    private static final String localIP = "http://127.0.0.1:9080";
    private TestService service = RestClientFactory.createClient(TestService.class);
    private TestServer server;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        server = new TestServer();
        server.start();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        server.stop();
    }

    public void testBaseUrlGetAndSet() {
        assertNotNull(service);
        RestClientInterface interf = (RestClientInterface) service;
        String baseUrl = interf.getBaseUrl();
        assertNotNull(baseUrl);
        assertTrue(localHost.equals(baseUrl));
        interf.setBaseUrl(localIP);
        assertTrue(localIP.equals(interf.getBaseUrl()));
    }

}
