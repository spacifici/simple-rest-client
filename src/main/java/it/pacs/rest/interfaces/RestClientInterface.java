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
package it.pacs.rest.interfaces;

import it.pacs.rest.annotatios.RestService;

/**
 * Declare methods to set and get base REST service properties and some utility method.<br/>
 * Each concrete object returned by {@link it.pacs.rest.factories.RestClientFactory} will implement this interface.
 *
 * @author Stefano Pacifici
 */
public interface RestClientInterface {

    /**
     * Return the base url assigned by {@link RestService} annotation
     *
     * @return the base url or null if no url was specified
     */
    String getBaseUrl();

    /**
     * Set the base url for this client
     *
     * @param baseUrl a string representing the base url
     */
    void setBaseUrl(String baseUrl);

    /**
     * It returns a special Service implementation that clean cache.
     *
     * @return an Object
     */
    Object cacheCleaner();
}
