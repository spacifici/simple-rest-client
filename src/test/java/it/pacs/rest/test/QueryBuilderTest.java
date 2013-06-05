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

import it.pacs.rest.factories.impl.QueryBuilder;
import junit.framework.TestCase;

/**
 * @author Stefano Pacifici
 *
 */
public class QueryBuilderTest extends TestCase {

	private static final String[] names = new String[] { "n", "s", "b", "f", "d", "a b" };
	private static final Object[] args  = new Object[] { 12, "abc", true, 12.3456789f, 1.23456789, "sarà l'aurora" };
	
	public void testQueryBuilder() {
		QueryBuilder builder = new QueryBuilder();
		for (int i = 0; i < 3; i++) {
			builder.add(names[i], i);
		}
		
		assertTrue("n=12&s=abc&b=true".equals(builder.getQueryString(args)));
	}
	
	public void testSpacesAndSpecial() {
		QueryBuilder builder = new QueryBuilder();
		builder.add(names[names.length-1], names.length - 1);
		builder.getQueryString(args);
	}
}
