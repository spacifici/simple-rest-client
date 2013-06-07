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
import it.pacs.rest.annotatios.Path;
import it.pacs.rest.annotatios.PathParam;
import it.pacs.rest.annotatios.QueryParam;
import it.pacs.rest.interfaces.RestClientInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP requests abstract base class, subclasses have to implement the {@link RestMethod#execute(Class, Object[])} method
 *
 * @author Stefano Pacifici
 */
public abstract class RestMethod {

    /**
     * Use it to convert json to object
     */
    protected static final Gson gson = new Gson();

    private Method method;
    private RestClientInterface handler;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private PathBuilder pathBuilder = null;

    public RestMethod(RestClientInterface handler, Method method) {
        this.handler = handler;
        this.method = method;

        Path path = method.getAnnotation(Path.class);
        if (path != null && !path.value().isEmpty()) {
            pathBuilder = new PathBuilder(path.value());
        }

        initBuilders();
    }

    /**
     * Init path and query builders by scan method parameters annotations
     */
    private void initBuilders() {
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramsAnnotations.length; i++) {
            for (Annotation annotation : paramsAnnotations[i]) {
                if (annotation.annotationType().equals(PathParam.class)
                        && pathBuilder != null) {
                    PathParam param = (PathParam) annotation;
                    pathBuilder.addParam(param.value(), i);
                }
                if (annotation.annotationType().equals(QueryParam.class)) {
                    QueryParam param = (QueryParam) annotation;
                    queryBuilder.addParam(param.value(), i);
                }
            }
        }
    }

    /**
     * Using PathBuilder and QueryBuilder, it build the url to the rest service
     * you want to call.
     *
     * @param args Those are the arguments passed to the interface method
     * @return an {@link URL} to the rest service
     * @throws UnsupportedEncodingException if UTF-8 codec is not supported
     * @throws MalformedURLException        if we do not obtain a valid URL by concatenating base url,
     *                                      path and query
     */
    protected URL buildUrl(Object[] args) throws UnsupportedEncodingException,
            MalformedURLException {
        StringBuilder builder = new StringBuilder();
        String baseUrl = handler.getBaseUrl();
        // Remove extra slashes at the end
        while (baseUrl.endsWith("/"))
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        builder.append(baseUrl);

        String path = pathBuilder != null ? pathBuilder.buildPath(args) : "";
        // Remove extra slashes at begin
        while (path.startsWith("/"))
            path = path.substring(1);
        if (!path.isEmpty())
            builder.append("/").append(path);

        String query = queryBuilder.buildQueryString(args);
        if (!query.isEmpty())
            builder.append("?").append(query);

        return new URL(builder.toString());
    }

    /**
     * @return the interface {@link Method} descriptor
     */
    protected Method getMethod() {
        return method;
    }


    /**
     * Execute the HTTP request associated with this method
     *
     * @param clazz the return type of the interface method
     * @param args  the arguments passed to the interface proxy
     * @return the object constructed by gson
     * @throws IOException
     */
    public abstract Object execute(Class clazz, Object[] args) throws IOException;
}
