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
package it.pacs.rest.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility functions
 *
 * @author Stefano Pacifici
 */
public class Utils {

    private final static char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final static Map<Class<?>, Object> DEFAULT_VALUES = new HashMap<Class<?>, Object>();

    /**
     * This class can't be instantiated
     */
    private Utils() {

    }

    /**
     * Calculate the MD5 of the given string
     *
     * @param str the string on which the MD5Sum will be calculated
     * @return the md5sum string
     */
    public static String getMD5Sum(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = digest.digest(str.getBytes());
            StringBuilder builder = new StringBuilder();

            for (Byte b : buffer) {
                int hb = (b & 0xf0) >> 4;
                int lb = (b & 0x0f);

                builder.append(HEXDIGITS[hb]).append(HEXDIGITS[lb]);
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * Return the default value for the given class or null if a default value for the class is not given
     *
     * @param clazz the class we query for a default value
     * @return a default value or null
     */
    public static <T> T getDefaultValue(Class<T> clazz) {
        return (T) DEFAULT_VALUES.get(clazz);
    }

    static {
        DEFAULT_VALUES.put(boolean.class, Boolean.FALSE);
        DEFAULT_VALUES.put(byte.class, (byte) 0);
        DEFAULT_VALUES.put(short.class, (short) 0);
        DEFAULT_VALUES.put(int.class, 0);
        DEFAULT_VALUES.put(long.class, 0L);
        DEFAULT_VALUES.put(char.class, '\0');
        DEFAULT_VALUES.put(float.class, 0.0F);
        DEFAULT_VALUES.put(double.class, 0.0);
    }
}
