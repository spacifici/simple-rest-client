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
import java.util.LinkedList;
import java.util.List;

/**
 * Helps to build query part of the url
 * 
 * @author Stefano Pacifici
 * 
 */
public class QueryBuilder {

	/**
	 * A couple of name and index
	 */
	static final class NameAndIndex {
		String name;
		int index;

		public NameAndIndex(String name, int index) {
			this.name = name;
			this.index = index;
		}
	}

	private List<NameAndIndex> params = new LinkedList<NameAndIndex>();
	
	/**
	 * Add a parameter name and its index to the query builder
	 * 
	 * @param name
	 *            parameter name
	 * @param index
	 *            parameter index
	 */
	public void add(String name, int index) {
		params.add(new NameAndIndex(name, index));
	}

	/**
	 * @param args
	 * @return
	 */
	public String getQueryString(Object[] args) {
		StringBuilder builder = new StringBuilder();
		String divider = "";
		for (NameAndIndex nai : params)
			if (nai.index < args.length) {
				try {
					String nameEnc = URLEncoder.encode(nai.name, "UTF-8");
					String paramEnc = URLEncoder.encode(paramToString(args[nai.index]), "UTF-8");
					builder.append(divider).append(nameEnc).append("=").append(paramEnc);
					divider = "&";
				} catch (UnsupportedEncodingException e) {
					// Nothing to do here;
				}
			}
		return builder.toString();
	}

	/**
	 * @param object
	 * @return
	 */
	private String paramToString(Object object) {
		return object.toString();
	}
}