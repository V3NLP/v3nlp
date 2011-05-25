package gov.va.vinci.v3nlp;

import junit.framework.TestCase;
import org.junit.Test;

public class XORCipherTest extends TestCase {

    @Test
    public void testIsValidNameNumbersAndCharacters() throws Exception {
        assert (XORCipher.encode("vhamaster\\vhaislcornir").trim().equals("Ez4jJS4GGBAcJE4pVxoDBQgKID9QJw=="));
        assert ("vhamaster\\vhaislcornir".equals(XORCipher.decode("Ez4jJS4GGBAcJE4pVxoDBQgKID9QJw==").trim()));
    }
}
