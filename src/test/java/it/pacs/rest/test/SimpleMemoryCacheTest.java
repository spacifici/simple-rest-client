package it.pacs.rest.test;

import it.pacs.rest.cache.SimpleMemoryCache;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Stefano Pacifi
 */
public class SimpleMemoryCacheTest extends TestCase {

    private static final String localhost = "http://localhost:8080/test";

    public void testPut() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        URL url = new URL(localhost);
        assertNotNull(cache.put(url, new ByteArrayInputStream(localhost.getBytes())));
        assertNotNull(cache.get(url));
        assertTrue(cache.getUpdateTime(url) <= System.currentTimeMillis());
    }

    public void testPutWithExpire() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        URL url = new URL(localhost);
        assertNotNull(cache.put(url, new ByteArrayInputStream(localhost.getBytes()), System.currentTimeMillis() - 10));
        assertNull(cache.get(url));
    }

    public void testPutToFill() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        int cacheSize = cache.cacheSize();
        byte[] data = new byte[cacheSize / 9];
        Arrays.fill(data, (byte) 1);

        for (int i = 0; i < 10; i++) {
            URL url = new URL("http://localhost/" + i);
            cache.put(url, new ByteArrayInputStream(data));
        }

        assertNotNull(cache.get(new URL("http://localhost/9")));
        assertNotNull(cache.get(new URL("http://localhost/1")));
        assertNull(cache.get(new URL("http://localhost/0")));
        assertEquals(cacheSize - cacheSize / 9 * 9, cache.availableCacheSize());
    }
}
