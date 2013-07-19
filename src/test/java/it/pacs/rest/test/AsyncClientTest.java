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
package it.pacs.rest.test;

import it.pacs.rest.factories.RestClientFactory;
import it.pacs.rest.interfaces.RestMethodCallback;
import it.pacs.rest.test.support.TestServer;
import it.pacs.rest.test.support.TestService;
import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Stefano Pacifici
 */
public class AsyncClientTest extends TestCase {

    TestServer server;

    private class TestRestMethodCallback implements RestMethodCallback {

        public CountDownLatch latch = new CountDownLatch(1);
        public Object result = null;
        public int id = Integer.MIN_VALUE;

        @Override
        public void onComplete(int id, Object result) {
            this.id = id;
            this.result = result;
            latch.countDown();
        }

        @Override
        public void onException(int id, Throwable throwable) {
            this.result = throwable;
            this.id = id;
            latch.countDown();
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        server = new TestServer();
        server.start();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        server.stop();
    }

    public void testAsyncGet() throws InterruptedException {
        TestRestMethodCallback listener = new TestRestMethodCallback();
        TestService service = RestClientFactory.getAsyncClient(TestService.class, listener);
        Object result = service.method1("hello", "12", "13", "q1", "q2");
        assertNull(result);
        listener.latch.await(5, TimeUnit.SECONDS);

        if (listener.latch.getCount() > 0)
            fail("Callbacks never called");

        assertNotNull(listener.result);
        assertEquals(TestService.METHOD1_ID, listener.id);
    }
}
