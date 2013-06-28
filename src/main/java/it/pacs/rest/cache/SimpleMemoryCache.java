package it.pacs.rest.cache;

import it.pacs.rest.interfaces.CacheInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Stefano Pacifici
 */
public class SimpleMemoryCache implements CacheInterface {

    // The default cache size (512kb)
    private static final int DEFAULT_CACHE_SIZE = 512 * 1024;
    // The internal cache
    private final LinkedHashMap<URL, CacheEntry> cache;
    // The max cache size
    private final int maxCacheSize;
    // The available cache space
    private int availableCacheSize;

    /**
     * Create a cache with default parameters
     */
    public SimpleMemoryCache() {
        cache = new LinkedHashMap<URL, CacheEntry>(1000, 1.1f, true);
        maxCacheSize = DEFAULT_CACHE_SIZE;
        availableCacheSize = DEFAULT_CACHE_SIZE;
    }

    /**
     * Like {@link it.pacs.rest.interfaces.CacheInterface#put(java.net.URL, java.io.InputStream)}, but add an
     * expiration time.
     *
     * @param url         the request url
     * @param inputStream an {@link java.io.InputStream} which data will be copied in the cache
     * @param expireTime  the time (as milliseconds since Gen. 1st, 1970 GMT) when the data will be considered
     *                    expired
     * @return an InputString backed by data copied in the cache, null if error an occurred
     */
    @Override
    public InputStream put(URL url, InputStream inputStream, long expireTime) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        byte[] data = outputStream.toByteArray();
        int size = data.length;

        // Don't cache data bigger than the half of the cache
        if (size > maxCacheSize / 2)
            return new ByteArrayInputStream(data);

        CacheEntry cacheEntry = new CacheEntry();
        cacheEntry.expireTime = expireTime;
        cacheEntry.data = data;
        cacheEntry.updateTime = System.currentTimeMillis();

        synchronized (cache) {
            availableCacheSize -= size;
            cache.put(url, cacheEntry);
            // Free cache
            if (availableCacheSize <= maxCacheSize / 10) {
                Iterator<Map.Entry<URL, CacheEntry>> iterator = cache.entrySet().iterator();
                while (iterator.hasNext() && availableCacheSize <= 0) {
                    Map.Entry<URL, CacheEntry> next = iterator.next();
                    availableCacheSize += next.getValue().data.length;
                    iterator.remove();
                }
            }
        }

        return new ByteArrayInputStream(cacheEntry.data);
    }

    /**
     * @return the cache size in bytes
     */
    @Override
    public int cacheSize() {
        return maxCacheSize;
    }

    /**
     * @return the available cache in bytes
     */
    @Override
    public int availableCacheSize() {
        return availableCacheSize;
    }

    /**
     * @param url the url associated with the cache element
     * @return the timestamp of the last {@link #put(java.net.URL, java.io.InputStream)} for the url, or -1 if the
     *         the url is not cached
     */
    @Override
    public long getUpdateTime(URL url) {
        CacheEntry cacheEntry;
        synchronized (cache) {
            cacheEntry = cache.get(url);
        }

        return cacheEntry != null ? cacheEntry.updateTime : -1l;
    }


    @Override
    public InputStream get(URL url) {
        CacheEntry cacheEntry;
        long now = System.currentTimeMillis();
        synchronized (cache) {
            cacheEntry = cache.get(url);
            if (cacheEntry != null && cacheEntry.expireTime >= 0 && cacheEntry.expireTime < now) {
                availableCacheSize += cacheEntry.data.length;
                cache.remove(url);
                cacheEntry = null;
            }
        }
        return cacheEntry != null ? new ByteArrayInputStream(cacheEntry.data) : null;
    }

    @Override
    public InputStream put(URL url, InputStream inputStream) {
        return put(url, inputStream, -1);
    }

    private class CacheEntry {
        public long expireTime;
        public byte[] data;
        public long updateTime;
    }
}
