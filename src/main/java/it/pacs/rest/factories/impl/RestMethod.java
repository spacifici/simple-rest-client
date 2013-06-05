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

import it.pacs.rest.annotatios.Path;
import it.pacs.rest.annotatios.PathParam;
import it.pacs.rest.annotatios.QueryParam;
import it.pacs.rest.interfaces.RestClientInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author Stefano Pacifici
 * 
 */
public class RestMethod {
	private Method method;
	private RestClientInterface handler;

	public RestMethod(RestClientInterface handler, Method method) {
		this.handler = handler;
		this.method = method;
	}

	private URL buildUrl(Object[] args) {
		Annotation[][] paramsAnnotations = method.getParameterAnnotations();
		if (paramsAnnotations.length != args.length)
			throw new RuntimeException("Wrong number of arguments");

		Path pathAnn = method.getAnnotation(Path.class);
		String path = pathAnn != null ? pathAnn.value() : null;
		for (int i = 0; i < args.length; i++) {
			for (Annotation annotation : paramsAnnotations[i]) {
				if (path != null && PathParam.class.equals(annotation)) {
					PathParam param = (PathParam) annotation;
					path = path.replace("{" + param.value() + "}",
							args[i].toString());
				}
			}
		}
		return null;
	}

	public Method getMethod() {
		return method;
	}
}
