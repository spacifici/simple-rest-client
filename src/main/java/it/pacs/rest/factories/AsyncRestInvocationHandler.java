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
package it.pacs.rest.factories;

import it.pacs.rest.annotation.AsyncID;
import it.pacs.rest.exceptions.RestMethodException;
import it.pacs.rest.interfaces.RestClientInterface;
import it.pacs.rest.interfaces.RestMethodCallback;
import it.pacs.rest.utils.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Stefano Pacifici
 */
public class AsyncRestInvocationHandler<T> implements InvocationHandler {

    T client;
    RestMethodCallback listener;

    public AsyncRestInvocationHandler(T client, RestMethodCallback listener) {
        this.client = client;
        this.listener = listener;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(RestClientInterface.class))
            return method.invoke(client, args);
        new Thread(new AsyncMethodRunnable(method, args), method.getName()).run();
        return Utils.getDefaultValue(method.getReturnType());
    }

    private class AsyncMethodRunnable implements Runnable {

        private Method method;
        private Object[] args;

        AsyncMethodRunnable(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        @Override
        public void run() {
            AsyncID annon = method.getAnnotation(AsyncID.class);
            int id = annon != null ? annon.value() : Integer.MIN_VALUE;
            try {
                Object result = method.invoke(client, args);
                listener.onComplete(id, result);
            } catch (RestMethodException e) {
                listener.onException(id, e.getCause());
            } catch (Exception e) {
                listener.onException(id, e);
            }
        }
    }
}
