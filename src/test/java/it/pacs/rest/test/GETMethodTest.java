/*
 * Copyright (C) 2013 Stefano Pacifici
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.pacs.rest.test;

import it.pacs.rest.factories.RestClientFactory;
import it.pacs.rest.test.support.TestServer;
import it.pacs.rest.test.support.TestService;
import junit.framework.TestCase;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Stefano Pacifici
 */
public class GETMethodTest extends TestCase {

    final static Map<String, String> paramsMap;
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

    public void testNormalGet() {
        TestService service = RestClientFactory.getClient(TestService.class);
        assertNotNull(service);
        Map<String, String> result = service.method1(paramsMap.get("hp"),
                paramsMap.get("pp1"),
                paramsMap.get("pp2"),
                paramsMap.get("qp1"),
                paramsMap.get("qp2"));
        assertNotNull(result);
        assertEquals(paramsMap.get("hp"), result.get("hp"));
        assertEquals(paramsMap.get("qp1"), result.get("qp1"));
        assertEquals(paramsMap.get("qp2"), result.get("qp2"));
        assertEquals("/test/method1/" + paramsMap.get("pp1") + "/code/" + paramsMap.get("pp2"), result.get("path"));
    }

    public void testCachedGet() {
        TestService service = RestClientFactory.getClient(TestService.class);
        assertNotNull(service);
        String methodUri = "/test/method2?param=12";
        String result1 = service.method2(12);
        int stat = testServer.getStats(methodUri);
        assertNotNull(result1);
        service.method2(12);
        assertEquals(stat + 1, testServer.getStats(methodUri));
    }

    static {
        paramsMap = new TreeMap<String, String>();
        paramsMap.put("hp", "HeaderParam");
        paramsMap.put("pp1", "PathParam1");
        paramsMap.put("pp2", "PathParam2");
        paramsMap.put("qp1", "QueryParam1");
        paramsMap.put("qp2", "QueryParam2");
    }
}
