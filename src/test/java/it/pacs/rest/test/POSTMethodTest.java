package it.pacs.rest.test;

import it.pacs.rest.factories.RestClientFactory;
import it.pacs.rest.test.support.Person;
import it.pacs.rest.test.support.TestServer;
import it.pacs.rest.test.support.TestService;
import junit.framework.TestCase;

import java.util.Map;

/**
 * @author: Stefano Pacifici
 */
public class POSTMethodTest extends TestCase{

    public static final String NAME = "Mickey";
    public static final String SURNAME = "Mouse";

    public TestServer server;
    public TestService service = RestClientFactory.createClient(TestService.class);

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
