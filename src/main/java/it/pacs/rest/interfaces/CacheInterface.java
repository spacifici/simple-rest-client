package it.pacs.rest.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Interface for cache object used by the library
 *
 * @author Stefano Pacifici
 */
public interface CacheInterface {

    /**
     * Like {@link CacheInterface#put(java.net.URL, java.io.InputStream)}, but add an expiration time.
     *
     * @param url         the request url
     * @param inputStream an {@link InputStream} which data will be copied in the cache
     * @param expireTime  the time (as milliseconds since Gen. 1st, 1970 GMT) when the data will be considered
     *                    expired
     * @return an InputString backed by data copied in the cache, null if error an occurred
     */
    InputStream put(URL url, InputStream inputStream, long expireTime) throws IOException;

    /**
     * Copy the {@link InputStream} in the cache
     *
     * @param url         the request url
     * @param inputStream an {@link InputStream} which data will be copied in the cache
     * @return an InputString backed by data copied in the cache, null if error an occurred
     */
    InputStream put(URL url, InputStream inputStream) throws IOException;

    /**
     * @param url the url associated with the cache element
     * @return an {@link InputStream} from which read the data
     */
    InputStream get(URL url);

    /**
     * @return the cache size in bytes
     */
    int cacheSize();

    /**
     * @return the available cache in bytes
     */
    int availableCacheSize();

    /**
     * @param url the url associated with the cache element
     * @return the timestamp of the last {@link #put(java.net.URL, java.io.InputStream)} for the url, or -1 if the
     *         the url is not cached
     */
    long getUpdateTime(URL url);
}
