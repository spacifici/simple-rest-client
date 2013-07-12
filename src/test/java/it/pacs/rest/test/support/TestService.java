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
package it.pacs.rest.test.support;

import it.pacs.rest.annotation.*;

import java.util.Map;

/**
 * @author Stefano Pacifici
 */
@RestService("http://localhost:9080")
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
    @Cache
    String method2(@QueryParam("param") int param);

    @POST
    @Path("/test/method3")
    Map<String, Object> method3(@NamedParam("param1") String param1, @NamedParam("param2") int param2, @NamedParam("param3") double param3);

    @POST
    @Path("/test/method4")
    Person method4(Person person);
}
