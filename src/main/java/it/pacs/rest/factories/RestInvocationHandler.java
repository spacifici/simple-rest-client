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
package it.pacs.rest.factories;

import com.squareup.okhttp.OkHttpClient;
import it.pacs.rest.annotation.GET;
import it.pacs.rest.annotation.POST;
import it.pacs.rest.annotation.RestService;
import it.pacs.rest.cache.SimpleMemoryCache;
import it.pacs.rest.factories.impl.GetMethod;
import it.pacs.rest.factories.impl.PostMethod;
import it.pacs.rest.factories.impl.RestMethod;
import it.pacs.rest.interfaces.CacheInterface;
import it.pacs.rest.interfaces.RestClientInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Stefano Pacifici
 */
class RestInvocationHandler<T> implements InvocationHandler,
        RestClientInterface {

    // The OkHttpClient
    private final static OkHttpClient client;
    // The cache (TODO May not be activated by default)
    private static CacheInterface cache;
    // The base url for all the requests
    private String baseUrl;
    // Maps method generic string to link RestMethod
    private HashMap<String, RestMethod> methods = new HashMap<String, RestMethod>();
    // The service interface
    private Class<T> serviceInterface;

    /**
     * Build the RestInvocationHandler for the given interface
     *
     * @param clazz the interface
     */
    public RestInvocationHandler(Class<T> clazz) {
        serviceInterface = clazz;
        RestService restService = clazz.getAnnotation(RestService.class);
        baseUrl = restService != null ? restService.value() : null;
        // Remove extra slashes at the end
        while (baseUrl != null && baseUrl.endsWith("/"))
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        initMethods();
        cache = new SimpleMemoryCache();
    }

    /**
     * For each method create the correct corresponding class
     */
    private void initMethods() {
        for (Method method : serviceInterface.getMethods()) {
            if (method.getAnnotation(GET.class) != null)
                methods.put(method.toGenericString(), new GetMethod(this, method));
            else if (method.getAnnotation(POST.class) != null)
                methods.put(method.toGenericString(), new PostMethod(this, method));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getDeclaringClass().equals(RestClientInterface.class))
            return method.invoke(this, args);
        RestMethod restMethod = methods.get(method.toGenericString());
        if (restMethod != null)
            return restMethod.execute(method.getReturnType(), args);
        return null;
    }

    /* (non-Javadoc)
     * @see it.pacs.rest.interfaces.RestClientInterface#getBaseUrl()
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /* (non-Javadoc)
     * @see it.pacs.rest.interfaces.RestClientInterface#setBaseUrl(java.lang.String)
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Open a connection to the server
     *
     * @param url the url to open
     * @return an {@link java.net.HttpURLConnection} instance
     */
    @Override
    public HttpURLConnection openConnection(URL url) {
        return client.open(url);
    }

    /**
     * @return the cache associated with this interface
     */
    @Override
    public CacheInterface getCache() {
        return cache;
    }

    static {
        client = new OkHttpClient();
    }
}
