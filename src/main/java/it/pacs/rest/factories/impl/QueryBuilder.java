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

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Helps to build query part of the url
 *
 * @author Stefano Pacifici
 */
public class QueryBuilder {

    private static Gson gson = new Gson();

    private List<NameIndex> params = new LinkedList<NameIndex>();

    /**
     * Add a parameter name and its index to the query builder
     *
     * @param name  parameter name
     * @param index parameter index
     */
    public void addParam(String name, int index) {
        params.add(new NameIndex(name, index));
    }

    /**
     * Build the query string, encoding it if is needed
     *
     * @param args Arguments to be associated
     * @return the eventually url encoded query string
     * @throws UnsupportedEncodingException
     */
    public String buildQueryString(Object[] args) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        String divider = "";
        for (NameIndex nai : params)
            if (nai.index < args.length) {
                String nameEnc = URLEncoder.encode(nai.name, "UTF-8");
                String paramEnc = URLEncoder.encode(
                        paramToString(args[nai.index]), "UTF-8");
                builder.append(divider).append(nameEnc).append("=")
                        .append(paramEnc);
                divider = "&";
            }
        return builder.toString();
    }

    /**
     * Convert the given object to a string
     *
     * @param object the object to convert
     * @return a string representation
     */
    private String paramToString(Object object) {
        if (object.getClass().isPrimitive() || object instanceof String)
            return object.toString();
        return gson.toJson(object);
    }
}