package gov.va.vinci.v3nlp;

import junit.framework.TestCase;
import org.junit.Test;

public class XORCipherTest extends TestCase {

      @Test
    public void testIsValidNameNumbersAndCharacters() throws Exception {
          System.out.println("Encoded=" + XORCipher.encode("vhamaster\\vhaislcornir"));
          assert("Ez4jJS4GGBAcJE4pVxoDBQgKID9QJw==".equals(XORCipher.encode("vhamaster\\vhaislcornir")));


      }
}
