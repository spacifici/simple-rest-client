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

import it.pacs.rest.interfaces.RestClientInterface;
import it.pacs.rest.interfaces.RestMethodCallback;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this class to create your REST client.<br/>
 *
 * @author Stefano Pacifici
 */
public class RestClientFactory {

    // Memorize the client
    private static final Map<Class, Object> clientsMap = new HashMap<Class, Object>();

    /**
     * Create a REST client that implements the given interface.<br/>
     * Every object returned by this factory also will implement {@link RestClientInterface}<br/>
     *
     * @param clazz The interface your rest service implements
     * @return a REST client as described by your interface
     */
    public static <T> T getClient(Class<T> clazz) {
        T client;
        synchronized (clientsMap) {
            client = (T) clientsMap.get(clazz);
            if (client == null) {
                client = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                        new Class<?>[]{clazz, RestClientInterface.class},
                        new RestInvocationHandler<T>(clazz));
                clientsMap.put(clazz, client);
            }
        }
        return client;
    }


    public static <T> T getAsyncClient(Class<T> clazz, RestMethodCallback listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener has not to be null");

        T syncClient = getClient(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz, RestClientInterface.class},
                new AsyncRestInvocationHandler<T>(syncClient, listener));
    }
}
