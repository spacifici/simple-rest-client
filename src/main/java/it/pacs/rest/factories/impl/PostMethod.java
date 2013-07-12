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

import it.pacs.rest.interfaces.RestClientInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Stefano Pacifici
 */
public class PostMethod extends RestMethod {

    public PostMethod(RestClientInterface restClient, Method method) {
        super(restClient, method);
    }

    /**
     * Execute the HTTP request associated with this method
     *
     * @param clazz the return type of the interface method
     * @param args  the arguments passed to the interface proxy
     * @return the object constructed by gson
     * @throws java.io.IOException
     */
    @Override
    public Object execute(Class clazz, Object[] args) throws IOException {
        URL url = buildUrl(args);
        Object result = null;
        HttpURLConnection connection = restClient.openConnection(url);
        addHeaderParameters(connection, args);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        OutputStream os = null;
        InputStream is = null;
        try {
            os = connection.getOutputStream();
            buildRequestContent(os, args);
            os.close();

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK && clazz != Void.class) {
                is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                result = gson.fromJson(reader, clazz);
                reader.close();
            }
        } finally {
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        }

        return result;
    }

}
