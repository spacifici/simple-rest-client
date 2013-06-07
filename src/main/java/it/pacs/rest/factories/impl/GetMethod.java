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

import it.pacs.rest.interfaces.RestClientInterface;

import java.io.IOException;
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

    /**
     * @param handler
     * @param method
     */
    public GetMethod(RestClientInterface handler, Method method) {
        super(handler, method);
    }

    @Override
    public Object execute(Class clazz, Object[] args) throws IOException {
        URL url = buildUrl(args);
        Object result = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        try {
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            result = gson.fromJson(reader, clazz);
            reader.close();
        } finally {
            connection.disconnect();
        }
        return result;
    }

}
