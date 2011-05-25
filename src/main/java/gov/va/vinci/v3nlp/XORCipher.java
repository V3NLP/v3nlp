package gov.va.vinci.v3nlp;

import org.apache.commons.codec.binary.Base64;

public class XORCipher {

        public static String KEY = "eVBHOulunx8A6spikeRQ9UEgyaXINTyzpn3SJ7FSzmwSlewTWI3";

        private static String xor(String source) {
            String key = KEY;
            String result = new String();
            for(int i = 0; i < source.length(); i++) {
                if(i > (key.length() - 1)) {
                    key += key;
                }
                result += (char)(source.charAt(i) ^ key.charAt(i));
            }
            return result;
        }

        public static String encode(String source) throws Exception {
            Base64 encoder = new  Base64 ();
            return encoder.encodeToString(xor(source).getBytes());
        }

        public static String decode(String source) throws Exception {
            Base64 encoder = new Base64 ();
            byte[] decoded = encoder.decode(source);
            return xor(new String(decoded));
        }

}
