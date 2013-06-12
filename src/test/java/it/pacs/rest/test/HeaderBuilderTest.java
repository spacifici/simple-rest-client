package it.pacs.rest.test;

import it.pacs.rest.factories.impl.HeaderBuilder;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Stefano Pacifici
 */
public class HeaderBuilderTest extends TestCase {

    public void testAddHeaders() throws IOException {
        HeaderBuilder builder = new HeaderBuilder();

        URL url = new URL("http://localhost");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        builder.addParam("string", 0);
        builder.addParam("number", 1);
        builder.addHeaders(connection, new Object[]{"test string", 12});

        assertEquals("test string", connection.getRequestProperty("string"));
        assertEquals("12", connection.getRequestProperty("number"));
    }
}
