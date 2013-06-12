/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package it.pacs.rest.factories.impl;

import it.pacs.rest.annotatios.Cached;
import it.pacs.rest.interfaces.CacheInterface;
import it.pacs.rest.interfaces.RestClientInterface;

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
        Cached cachedAnnotation = method.getAnnotation(Cached.class);
        isCached = cachedAnnotation != null;
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
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        CacheInterface cache = isCached ? restClient.getCache() : null;
        if (cache != null) {
            long timestamp = cache.getUpdateTime(url);
            if (timestamp > 0)
                connection.setIfModifiedSince(timestamp);
        }
        addHeaderParameters(connection, args);
        connection.connect();
        try {
            int status = connection.getResponseCode();
            InputStream data;
            switch (status) {
                case HttpURLConnection.HTTP_OK:
                    data = connection.getInputStream();
                    if (cache != null) {
                        InputStream cacheInputStream = cache.put(url, data);
                        data.close();
                        data = cacheInputStream;
                    }
                    break;
                case HttpURLConnection.HTTP_NOT_MODIFIED:
                    data = cache != null ? cache.get(url) : null;
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
}
