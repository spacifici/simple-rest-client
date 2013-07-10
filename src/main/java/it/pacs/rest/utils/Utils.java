package it.pacs.rest.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility functions
 *
 * @author: Stefano Pacifici
 */
public class Utils {

    private static char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
}