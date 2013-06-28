package it.pacs.rest.test;

import it.pacs.rest.cache.SimpleMemoryCache;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * @author Stefano Pacifi
 */
public class SimpleMemoryCacheTest extends TestCase {

    private static final String localhost = "http://localhost:8080/test";

    public void testPut() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        assertNotNull(cache.put(localhost, "0000000", new ByteArrayInputStream(localhost.getBytes())));
        assertNotNull(cache.get(localhost));
        assertTrue(cache.getUpdateTime(localhost) <= System.currentTimeMillis());
    }

    public void testPutWithExpire() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        assertNotNull(cache.put(localhost, "0000000", new ByteArrayInputStream(localhost.getBytes()), System.currentTimeMillis() - 10));
        assertNull(cache.get(localhost));
    }

    public void testPutToFill() throws MalformedURLException {
        SimpleMemoryCache cache = new SimpleMemoryCache();
        int cacheSize = cache.cacheSize();
        byte[] data = new byte[cacheSize / 9];
        Arrays.fill(data, (byte) 1);

        for (int i = 0; i < 10; i++) {
            cache.put("http://localhost/" + i, "0000000", new ByteArrayInputStream(data));
        }

        assertNotNull(cache.get("http://localhost/9"));
        assertNotNull(cache.get("http://localhost/1"));
        assertNull(cache.get("http://localhost/0"));
        assertEquals(cacheSize - cacheSize / 9 * 9, cache.availableCacheSize());
    }
}
