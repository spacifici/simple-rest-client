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

import it.pacs.rest.annotatios.*;

import java.util.Map;

/**
 * @author Stefano Pacifici
 */
@RestService("http://localhost:8080")
public interface TestService {
    @GET
    @Path("/test/method1/{pp1}/code/{pp2}")
    Map<String, String> method1(@HeaderParam("hp") String headerParam,
                                @PathParam("pp1") String pathParam1,
                                @PathParam("pp2") String pathParam2,
                                @QueryParam("qp1") String queryParam1,
                                @QueryParam("qp2") String queryParam2);

    @GET
    @Path("/test/method2")
    @Cached
    String method2(@QueryParam("param") int param);
}
