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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Utility class to build the url path
 *
 * @author Stefano Pacifici
 */
public class PathBuilder {

    interface PathPart {
        String getPart(Object[] args) throws UnsupportedEncodingException;
    }

    static final class StringPart implements PathPart {

        private String part;

        /*
         * (non-Javadoc)
         *
         * @see
         * it.pacs.rest.factories.impl.PathBuilder.PathPart#getPart(java.lang
         * .Object[])
         */
        @Override
        public String getPart(Object[] args) {
            return part;
        }

        /**
         *
         */
        public StringPart(String part) {
            this.part = part;
        }
    }

    final class ParamPart implements PathPart {

        private final String name;

        /**
         * @param name the parameter name
         */
        public ParamPart(String name) {
            this.name = name;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * it.pacs.rest.factories.impl.PathBuilder.PathPart#getPart(java.lang
         * .Object[])
         */
        @Override
        public String getPart(Object[] args)
                throws UnsupportedEncodingException {
            Integer index = paramsIndexes.get(name);
            Object object = index != null && index < args.length ? args[index]
                    : null;
            if (object instanceof Number)
                return object.toString();
            if (object instanceof String)
                return URLEncoder.encode((String) object, "UTF-8");
            throw new IllegalArgumentException(
                    "Wrong parameter "
                            + name
                            + ". Path parameters have to be a number or a string");
        }

    }

    // Keep track of parameter name -> index relationship
    private HashMap<String, Integer> paramsIndexes = new HashMap<String, Integer>();

    private LinkedList<PathPart> parts;

    /**
     * Build a new PathBuilder by parsing the given path
     *
     * @param path a string containing the raw path (ie
     *             "/root/{param1}/{param2}")
     */
    public PathBuilder(String path) {
        String parsePath = path;
        parts = new LinkedList<PathPart>();
        while (!parsePath.isEmpty()) {
            int index = parsePath.indexOf('{');
            if (index < 0) {
                parts.add(new StringPart(parsePath));
                parsePath = "";
            } else if (index == 0) {
                int l = parsePath.indexOf('}');
                if (l < 0)
                    throw new IllegalArgumentException("Malformed path " + path);
                String param = parsePath.substring(1, l);
                parts.add(new ParamPart(param));
                parsePath = parsePath.substring(l + 1);
            } else {
                parts.add(new StringPart(parsePath.substring(0, index)));
                parsePath = parsePath.substring(index);
            }
        }
    }

    /**
     * Add a parameter and an associated index to the {@link PathBuilder}
     *
     * @param name  the parameter name
     * @param index the index inside the {@link PathBuilder#buildPath(Object[])} object array parameter
     */
    public void addParam(String name, int index) {
        paramsIndexes.put(name, index);
    }

    /**
     * Build the path by substitute the indexed parameters in the path template string
     *
     * @param args an object array containing the parameters to fill the template
     * @return the path
     * @throws UnsupportedEncodingException
     */
    public String buildPath(Object[] args) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (PathPart part : parts) {
            builder.append(part.getPart(args));
        }
        return builder.toString();
    }

}
