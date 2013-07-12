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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Stefano Pacifici
 */
public class ContentBuilder {

    private final static Gson gson = new Gson();

    /**
     * An unnamed parameter to be put in the request body
     */
    private static class ContentParam {
        public final int index;

        public ContentParam(int index) {
            this.index = index;
        }
    }

    /**
     * A named parameter to be put in the request
     */
    private static class NamedContentParam extends ContentParam {
        public final String name;

        public NamedContentParam(String name, int index) {
            super(index);
            this.name = name;
        }
    }

    // The parameter->index list
    private List<ContentParam> params = new LinkedList<ContentParam>();

    // Have this builder unnamed params?
    private boolean hasUnnamedParams = false;

    /**
     * Add a parameter to this content builder
     *
     * @param name  the name of the parameter
     * @param index the index inside the arguments array
     */
    public void addNamedParam(String name, int index) {
        params.add(new NamedContentParam(name, index));
    }

    /**
     * Add an unnamed parameter to this content builder
     *
     * @param index the index inside the arguments array
     */
    public void addUnnamedParam(int index) {
        hasUnnamedParams = true;
        params.add(new ContentParam(index));
    }

    /**
     * Build the body (content) of a POST (or PUT or PATCH) request
     *
     * @param args the method arguments array
     * @return the request content as a String
     */
    public String buildContent(Object[] args) {
        Object request;
        if (hasUnnamedParams) {
            // Unnamed parameters have priority on named ones
            if (params.size() == 1) {
                // Directly serialize the only parameter
                request = args[params.get(0).index];
            } else {
                // All parameters will be inserted in an array
                Object[] parameters = new Object[params.size()];
                int i = 0;
                for (ContentParam param : params) {
                    parameters[i] = args[param.index];
                    i++;
                }
                request = parameters;
            }
        } else {
            // Build an map name -> value
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for (ContentParam param : params) {
                NamedContentParam named = (NamedContentParam) param;
                map.put(named.name, args[named.index]);
            }
            request = map;
        }
        return gson.toJson(request);
    }
}
