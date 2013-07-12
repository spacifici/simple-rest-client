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

import it.pacs.rest.factories.impl.QueryBuilder;
import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author Stefano Pacifici
 */
public class QueryBuilderTest extends TestCase {

    private static final String[] names = new String[]{"n", "s", "b", "f",
            "d", "a b"};
    private static final Object[] args = new Object[]{12, "abc", true,
            12.3456789f, 1.23456789, "sarà l'aurora"};

    private static final String expectedSpaceAndSpecialsTestResult = ""
            + "a+b=sar%C3%A0+l%27aurora";

    private static final String expectedMapTestResult = "param=%7B%22f%22"
            + "%3A12.345679%2C%22d%22%3A1.23456789%2C%22a+b%22%3A%22sar%C3%"
            + "A0+l%5Cu0027aurora%22%2C%22b%22%3Atrue%2C%22s%22%3A%22abc%22"
            + "%2C%22n%22%3A12%7D";

    public void testQueryBuilder() {
        QueryBuilder builder = new QueryBuilder();
        for (int i = 0; i < 3; i++) {
            builder.addParam(names[i], i);
        }
        try {
            assertTrue("n=12&s=abc&b=true".equals(builder.buildQueryString(args)));
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }

    public void testSpacesAndSpecial() {
        QueryBuilder builder = new QueryBuilder();
        builder.addParam(names[names.length - 1], names.length - 1);
        String result = null;
        try {
            result = builder.buildQueryString(args);
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertTrue(expectedSpaceAndSpecialsTestResult.equals(result));
    }

    public void testMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < names.length && i < args.length; i++)
            map.put(names[i], args[i]);
        QueryBuilder builder = new QueryBuilder();
        builder.addParam("param", 0);
        String result = null;
        try {
            result = builder.buildQueryString(new Object[]{map});
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertTrue(expectedMapTestResult.equals(result));
    }
}
