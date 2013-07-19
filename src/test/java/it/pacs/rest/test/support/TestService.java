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
@SuppressWarnings("UnusedDeclaration")
@RestService("http://localhost:9080")
public interface TestService {

    public static final int METHOD1_ID = 1;
    public static final int METHOD2_ID = 2;
    public static final int METHOD3_ID = 3;
    public static final int METHOD4_ID = 4;

    @GET
    @Path("/test/method1/{pp1}/code/{pp2}")
    @AsyncID(METHOD1_ID)
    Map<String, String> method1(@HeaderParam("hp") String headerParam,
                                @PathParam("pp1") String pathParam1,
                                @PathParam("pp2") String pathParam2,
                                @QueryParam("qp1") String queryParam1,
                                @QueryParam("qp2") String queryParam2);

    @GET
    @Path("/test/method2")
    @Cache
    @AsyncID(METHOD2_ID)
    String method2(@QueryParam("param") int param);

    @POST
    @Path("/test/method3")
    @AsyncID(METHOD3_ID)
    Map<String, Object> method3(@NamedParam("param1") String param1, @NamedParam("param2") int param2, @NamedParam("param3") double param3);

    @POST
    @Path("/test/method4")
    @AsyncID(METHOD4_ID)
    Person method4(Person person);

    @DELETE
    @Path("/person/{id}/delete")
    void deleteMethod(@QueryParam("id") int id);

    @PUT
    @Path("/person/{id}")
    void putMethod(@PathParam("id") int id, Person person);

    @PATCH
    @Path("/person/{id}")
    void patchMethod(@PathParam("id") int id, @NamedParam("name") String name);
}
