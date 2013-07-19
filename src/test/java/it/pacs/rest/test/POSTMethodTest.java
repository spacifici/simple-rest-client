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
import it.pacs.rest.test.support.Person;
import it.pacs.rest.test.support.TestServer;
import it.pacs.rest.test.support.TestService;
import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Stefano Pacifici
 */
public class POSTMethodTest extends TestCase {

    public static final String NAME = "Mickey";
    public static final String SURNAME = "Mouse";

    public TestServer server;
    public TestService service = RestClientFactory.getClient(TestService.class);

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

    public void testNamedParameters() {
        Map<String, Object> result = service.method3("Hello world", 12, 12.13);
        assertNotNull(result);
        assertEquals(String.class, result.get("param1").getClass());
        // TODO why gson parse back a double instead of an integer
        assertTrue(result.get("param2") instanceof Number);
        assertTrue(result.get("param3") instanceof Number);
    }

    public void testSingleUnnamedParameter() {
        Person person = service.method4(new Person(NAME, SURNAME));
        assertNotNull(person);
        assertEquals(NAME, person.getName());
        assertEquals(SURNAME, person.getSurname());
    }
}
