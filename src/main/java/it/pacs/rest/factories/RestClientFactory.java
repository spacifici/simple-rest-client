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
package it.pacs.rest.factories;

import it.pacs.rest.interfaces.RestClientInterface;

import java.lang.reflect.Proxy;

/**
 * Use this class to create your REST client.<br/>
 *
 * @author Stefano Pacifici
 */
public class RestClientFactory {

    /**
     * Create a REST client that implements the given interface.<br/>
     * Every object returned by this factory also will implement {@link RestClientInterface}<br/>
     *
     * @param clazz The interface your rest service implements
     * @return a REST client as described by your interface
     */
    public static <T> T createClient(Class<T> clazz) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz, RestClientInterface.class},
                new RestInvocationHandler<T>(clazz));
    }

}
