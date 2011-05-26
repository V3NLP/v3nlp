package gov.va.vinci.v3nlp;

import org.apache.commons.validator.GenericValidator;

public class Utilities {
    public static String getUsernameAsDirectory(String userToken) {
        if (GenericValidator.isBlankOrNull(userToken)) {
            return "";
        } else {
            return XORCipher.decode(userToken).replace("\\", "-") + "/";
        }
    }
}
