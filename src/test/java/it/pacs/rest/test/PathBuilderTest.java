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
package it.pacs.rest.test;

import it.pacs.rest.factories.impl.PathBuilder;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;

/**
 * @author Stefano Pacifici
 */
public class PathBuilderTest extends TestCase {
    final static String path = "/test/{param1}/test/{param2}/us_{param1}";
    final static String expectedResult1 = "/test/42/test/abc/us_42";
    final static String expectedResult2 = "/test/23/test/a+b+c/us_23";

    public void testBuilder() {
        PathBuilder builder = new PathBuilder(path);
        String result = null;
        builder.addParam("param1", 0);
        builder.addParam("param2", 1);

        try {
            result = builder.buildPath(new Object[]{42, "abc"});
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertTrue(expectedResult1.equals(result));

        try {
            result = builder.buildPath(new Object[]{23, "a b c"});
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertTrue(expectedResult2.equals(result));
    }
}
