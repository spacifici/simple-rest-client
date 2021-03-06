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
package it.pacs.rest.interfaces;

import it.pacs.rest.annotation.RestService;

import java.net.HttpURLConnection;
import java.net.URL;

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
     * Open a connection to the server
     *
     * @param url the url to open
     * @return an {@link HttpURLConnection} instance
     */
    HttpURLConnection openConnection(URL url);

    /**
     * @return the cache associated with this interface
     */
    CacheInterface getCache();

}
