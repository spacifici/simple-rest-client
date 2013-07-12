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
package it.pacs.rest.factories.impl;

import it.pacs.rest.annotation.Cache;
import it.pacs.rest.interfaces.CacheInterface;
import it.pacs.rest.interfaces.RestClientInterface;
import it.pacs.rest.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implements the basic GET method
 *
 * @author Stefano Pacifici
 */
public class GetMethod extends RestMethod {

    // Does the method support caching
    private final boolean isCached;

    /**
     * Construct a new GetMethod
     *
     * @param restClient the underlying {@link RestClientInterface}
     * @param method     the java method descriptor
     */
    public GetMethod(RestClientInterface restClient, Method method) {
        super(restClient, method);

        // Fetch the reference for future use
        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        isCached = cacheAnnotation != null;
    }

    /**
     * Execute the GET request
     *
     * @param clazz the return type of the interface method
     * @param args  the arguments passed to the interface proxy
     * @return The fetched object, the previously obtained object or null if an error occurred
     * @throws IOException
     */
    @Override
    public Object execute(Class clazz, Object[] args) throws IOException {
        URL url = buildUrl(args);
        Object result = null;
        HttpURLConnection connection = restClient.openConnection(url);
        CacheInterface cache = isCached ? restClient.getCache() : null;
        String key = null;
        if (cache != null) {
            key = buildCacheKey(args);
            String etag = cache.getETag(key);
            if (etag != null)
                connection.setRequestProperty("If-None-Match", etag);
        }
        addHeaderParameters(connection, args);
        connection.connect();
        try {
            int status = connection.getResponseCode();
            InputStream data;
            switch (status) {
                case HttpURLConnection.HTTP_OK:
                    String etag = connection.getHeaderField("ETag");
                    data = connection.getInputStream();
                    //noinspection ConstantConditions
                    if (key != null && cache != null && etag != null) {
                        InputStream cacheInputStream = cache.put(key, etag, data);
                        data.close();
                        data = cacheInputStream;
                    }
                    break;
                case HttpURLConnection.HTTP_NOT_MODIFIED:
                    data = cache != null ? cache.get(key) : null;
                    break;
                default:
                    return null;
            }
            if (data == null)
                throw new IOException("Error fetching data");
            InputStreamReader reader = new InputStreamReader(data);
            result = gson.fromJson(reader, clazz);
            reader.close();
        } finally {
            connection.disconnect();
        }
        return result;
    }

    /**
     * Build a unique string to be used as key for the cache.
     *
     * @param args arguments passed to the method
     * @return an MD5 hash
     */
    private String buildCacheKey(Object[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.toGenericString()).append(restClient.getBaseUrl());
        for (Object arg : args)
            builder.append(arg);
        return Utils.getMD5Sum(builder.toString());
    }
}
