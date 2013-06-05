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
package it.pacs.rest.test.support;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * @author Stefano Pacifici
 * 
 */
public class TestServer {

	Server server = new Server(8080);

	public TestServer() {
		server.setHandler(new TestHandler());
	}

	public void start() throws Exception {
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

	private static class TestHandler extends AbstractHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jetty.server.Handler#handle(java.lang.String,
		 * org.eclipse.jetty.server.Request,
		 * javax.servlet.http.HttpServletRequest,
		 * javax.servlet.http.HttpServletResponse)
		 */
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			PrintWriter writer = response.getWriter();
			writer.append("Hello, world!!!");
		}

	}
}
