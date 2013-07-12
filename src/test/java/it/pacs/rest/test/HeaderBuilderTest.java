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
package it.pacs.rest.test;

import it.pacs.rest.factories.impl.HeaderBuilder;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Stefano Pacifici
 */
public class HeaderBuilderTest extends TestCase {

    public void testAddHeaders() throws IOException {
        HeaderBuilder builder = new HeaderBuilder();

        URL url = new URL("http://localhost");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        builder.addParam("string", 0);
        builder.addParam("number", 1);
        builder.addHeaders(connection, new Object[]{"test string", 12});

        assertEquals("test string", connection.getRequestProperty("string"));
        assertEquals("12", connection.getRequestProperty("number"));
    }
}
