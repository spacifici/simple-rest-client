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
package it.pacs.rest.interfaces;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for cache object used by the library
 *
 * @author Stefano Pacifici
 */
public interface CacheInterface {

    /**
     * Like {@link CacheInterface#put(String, String, java.io.InputStream)}, but add an expiration time.
     *
     * @param key         the request url
     * @param etag        the response etag
     * @param inputStream an {@link InputStream} which data will be copied in the cache
     * @param expireTime  the time (as milliseconds since Gen. 1st, 1970 GMT) when the data will be considered
     *                    expired
     * @return an InputString backed by data copied in the cache, null if error an occurred
     */
    InputStream put(String key, String etag, InputStream inputStream, long expireTime) throws IOException;

    /**
     * Copy the {@link InputStream} in the cache
     *
     * @param key         the cache key
     * @param etag        the response etag
     * @param inputStream an {@link InputStream} which data will be copied in the cache
     * @return an InputString backed by data copied in the cache, null if error an occurred
     */
    InputStream put(String key, String etag, InputStream inputStream) throws IOException;

    /**
     * @param key the url associated with the cache element
     * @return an {@link InputStream} from which read the data
     */
    InputStream get(String key);

    /**
     * @return the cache size in bytes
     */
    int cacheSize();

    /**
     * @return the available cache in bytes
     */
    int availableCacheSize();

    /**
     * @param key the cache key
     * @return the timestamp of the last {@link #put(String, String, java.io.InputStream)} for the url, or -1 if the
     *         the url is not cached
     */
    long getUpdateTime(String key);

    /**
     * Return the etag associated with the a previous request
     *
     * @param key the cache key
     * @return the etag
     */
    String getETag(String key);
}
