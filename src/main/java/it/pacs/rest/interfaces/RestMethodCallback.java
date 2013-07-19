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

/**
 * @author Stefano Pacifici
 */
public interface RestMethodCallback {

    /**
     * Called on successfully completed request
     *
     * @param id     the async method id (if the method has one)
     * @param result the result
     */
    void onComplete(int id, Object result);

    /**
     * Called if the request fails with an exception
     *
     * @param id        the async method id (if the method has one)
     * @param throwable the thrown exception
     */
    void onException(int id, Throwable throwable);

}
