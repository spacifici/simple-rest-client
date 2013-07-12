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

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class quickly add headers to an {@link java.net.HttpURLConnection}
 *
 * @author Stefano Pacifici
 */
public class HeaderBuilder {

    private List<NameIndex> headerParams = new ArrayList<NameIndex>();

    /**
     * Add a parameter name to the list of headers associated with the given index
     *
     * @param name  the header name (ie. Accept)
     * @param index the index in arguments array passed to the method
     */
    public void addParam(String name, int index) {
        headerParams.add(new NameIndex(name, index));
    }

    /**
     * Add the previously added header to the given connection
     *
     * @param connection the connection to set headers to
     * @param args       the arguments array passed to the method
     */
    public void addHeaders(HttpURLConnection connection, Object[] args) {
        for (NameIndex nai : headerParams) {
            if (nai.index < args.length)
                connection.addRequestProperty(nai.name, args[nai.index].toString());
        }
    }
}
