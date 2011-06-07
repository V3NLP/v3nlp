package gov.va.vinci.v3nlp;

import org.junit.Test;

public class UtilitiesTest {

    @Test
    public void testUserNameDirectory() {

        assert (Utilities.getUsernameAsDirectory(null).equals(""));
        assert (Utilities.getUsernameAsDirectory("").equals(""));

        assert (Utilities.getUsernameAsDirectory("Mx4DBQ4mODA8JE4pVwAcCgQXPDhL").equals("VHAMASTER-vhaslcornir/"));
    }
}

