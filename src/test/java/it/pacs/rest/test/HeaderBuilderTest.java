package it.pacs.rest.test;

import it.pacs.rest.factories.impl.HeaderBuilder;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: stefano
 * Date: 08/06/13
 * Time: 12.52
 * To change this template use File | Settings | File Templates.
 */
public class HeaderBuilderTest extends TestCase {

    public void testHeaderBuilder() throws IOException {
        HeaderBuilder builder = new HeaderBuilder();

        URL url = new URL("http://localhost");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        builder.addHeaders(connection, new Object[] { "test string", 12 });

    }
}
