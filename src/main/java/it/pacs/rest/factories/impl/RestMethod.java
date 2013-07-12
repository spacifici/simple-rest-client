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

import com.google.gson.Gson;
import it.pacs.rest.annotation.*;
import it.pacs.rest.interfaces.RestClientInterface;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP requests abstract base class, subclasses have to implement the {@link RestMethod#execute(Class, Object[])}
 * method
 *
 * @author Stefano Pacifici
 */
public abstract class RestMethod {

    /**
     * Use it to convert json to object
     */
    protected static final Gson gson = new Gson();
    protected final Method method;
    protected final RestClientInterface restClient;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private PathBuilder pathBuilder = null;
    private HeaderBuilder headerBuilder = new HeaderBuilder();
    private ContentBuilder contentBuilder = new ContentBuilder();

    public RestMethod(RestClientInterface restClient, Method method) {
        this.restClient = restClient;
        this.method = method;

        Path path = method.getAnnotation(Path.class);
        if (path != null && !path.value().isEmpty()) {
            pathBuilder = new PathBuilder(path.value());
        }

        initParameters();
    }

    /**
     * PathBuilderInit path builder, query builder and headers map by scan method parameters annotations
     */
    private void initParameters() {
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramsAnnotations.length; i++) {
            if (paramsAnnotations[i].length == 0) {
                contentBuilder.addUnnamedParam(i);
            } else {
                for (Annotation annotation : paramsAnnotations[i]) {
                    Class<?> annotationType = annotation.annotationType();
                    if (annotationType.equals(PathParam.class)
                            && pathBuilder != null) {
                        PathParam param = (PathParam) annotation;
                        pathBuilder.addParam(param.value(), i);
                    } else if (annotationType.equals(QueryParam.class)) {
                        QueryParam param = (QueryParam) annotation;
                        queryBuilder.addParam(param.value(), i);
                    } else if (annotationType.equals(HeaderParam.class)) {
                        HeaderParam param = (HeaderParam) annotation;
                        headerBuilder.addParam(param.value(), i);
                    } else if (annotationType.equals(NamedParam.class)) {
                        NamedParam param = (NamedParam) annotation;
                        contentBuilder.addNamedParam(param.value(), i);
                    } else {
                        contentBuilder.addUnnamedParam(i);
                    }
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
        String baseUrl = restClient.getBaseUrl();
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
     * Add header parameters to the request
     *
     * @param connection the request connection to fill
     * @param args       method arguments array
     */
    protected void addHeaderParameters(HttpURLConnection connection, Object[] args) {
        headerBuilder.addHeaders(connection, args);
    }

    /**
     * Attach content to the request (used for POST, PUT or PATH)
     *
     * @param stream the {@link OutputStream} to write the content to
     * @param args   the arguments array from which the content is built
     * @throws IOException
     */
    protected void buildRequestContent(OutputStream stream, Object[] args) throws IOException {
        String body = contentBuilder.buildContent(args);
        stream.write(body.getBytes());
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
