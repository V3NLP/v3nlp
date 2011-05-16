package gov.va.vinci.v3nlp;

import junit.framework.TestCase;
import org.junit.Test;

public class NlpUtilitiesTest  extends TestCase {

      @Test
    public void testIsValidNameNumbersAndCharacters() {
          assert(NlpUtilities.isValidNameNumbersAndCharacters("abcd1234rf_"));

          assert(!NlpUtilities.isValidNameNumbersAndCharacters("ab cd1234rf_"));

          assert(!NlpUtilities.isValidNameNumbersAndCharacters("~!abcd1234rf_"));
      }
}
