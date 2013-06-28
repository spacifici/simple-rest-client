package it.pacs.rest.test;

import it.pacs.rest.utils.Utils;
import junit.framework.TestCase;

/**
 * @author: Stefano Pacifici
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
