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

import it.pacs.rest.annotatios.RestService;
import it.pacs.rest.factories.impl.GetMethod;
import it.pacs.rest.factories.impl.RestMethod;
import it.pacs.rest.interfaces.RestClientInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Stefano Pacifici
 */
class RestInvocationHandler<T> implements InvocationHandler,
        RestClientInterface {

    // The base url for all the requests
    private String baseUrl;
    // Maps method generic string to link RestMethod
    private HashMap<String, RestMethod> methods = new HashMap<String, RestMethod>();


    /**
     * Build the RestInvocationHandler for the given interface
     *
     * @param clazz the interface
     */
    public RestInvocationHandler(Class<T> clazz) {
        RestService restService = clazz.getAnnotation(RestService.class);
        baseUrl = restService != null ? restService.value() : null;

        for (Method method : clazz.getMethods()) {
            methods.put(method.toGenericString(), new GetMethod(this, method));
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

}
