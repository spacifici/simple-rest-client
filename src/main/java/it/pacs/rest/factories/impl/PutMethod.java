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
package it.pacs.rest.factories.impl;

import it.pacs.rest.interfaces.RestClientInterface;

import java.lang.reflect.Method;

/**
 * Implements the HTTP PUT method
 *
 * @author Stefano Pacifici
 */
public class PutMethod extends PostMethod {

    public PutMethod(RestClientInterface restClient, Method method) {
        super(restClient, method);
    }

    @Override
    HTTPMethod getMethod() {
        return HTTPMethod.PUT;
    }
}
