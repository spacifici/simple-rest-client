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

import it.pacs.rest.utils.Utils;
import junit.framework.TestCase;

/**
 * @author Stefano Pacifici
 */
public class UtilsTest extends TestCase {

    public void testGetMD5() {
        String testStr = "test string 1234567890";
        String testMD5 = "8b4a0731bac4e032bb7ac56975977dda";

        String result = Utils.getMD5Sum(testStr);
        assertNotNull(result);
        assertTrue(testMD5.equals(result));
    }
}
