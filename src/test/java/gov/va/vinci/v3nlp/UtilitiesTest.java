package gov.va.vinci.v3nlp;

import org.junit.Test;

public class UtilitiesTest {
    private static String testString="<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
            "<!--  This file contains header definitions for Sectionizer NLP component -->\n" +
            "<headers>\t\n" +
            "\t<header categories=\"OTHER\" captGroupNum=\"0\" >\n" +
            "\t\t<![CDATA[(?i)\\(BILATERAL\\)\\s{1,3}CAROTID\\s{0,3}(:|;)]]>\n" +
            "\t</header>\n" +
            "\t<header categories=\"OTHER\" captGroupNum=\"0\" >\n" +
            "\t\t<![CDATA[(?i)\\(BILATERAL\\)\\s{1,3}FEMORAL\\s{0,3}(:|;)]]>\n" +
            "\t</header>\n" +
            "\t<header categories=\"OTHER\" captGroupNum=\"0\" >\n" +
            "\t\t<![CDATA[(?i)ABD\\s{0,3}(:|;)]]>\n" +
            "\t</header>\n" +
            "\t<header categories=\"COMMENTS\" captGroupNum=\"0\" >\n" +
            "\t\t<![CDATA[(?i)COMMENTS:]]>\n" +
            "\t</header>\n" +
            "\t<header categories=\"OTHER, HIST\" captGroupNum=\"0\" >\n" +
            "\t\t<![CDATA[(?i)ABUSE\\s{1,3}HISTORY\\s{0,3}(:|;)]]>\n" +
            "\t</header>\n" +
            "\t<includes>\n" +
            "\t\t<include>OTHER</include>\n" +
            "\t\t<include>COMMENTS</include> " +
            "\t</includes>\n" +
            "\t<excludes>\n" +
            "\t\t<exclude>HIST</exclude>\n" +
            "\t</excludes>\n" +
            "</headers>";

    private static String sectionizerConfigCreatorTest1_Output=
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<headers>\n" +
                    "<header captGroupNum=\"0\" categories=\"OTHER\">\n" +
                    "\t\t<![CDATA[(?i)\\(BILATERAL\\)\\s{1,3}CAROTID\\s{0,3}(:|;)]]>\n" +
                    "\t</header>\n" +
                    "<header captGroupNum=\"0\" categories=\"OTHER\">\n" +
                    "\t\t<![CDATA[(?i)\\(BILATERAL\\)\\s{1,3}FEMORAL\\s{0,3}(:|;)]]>\n" +
                    "\t</header>\n" +
                    "<header captGroupNum=\"0\" categories=\"OTHER\">\n" +
                    "\t\t<![CDATA[(?i)ABD\\s{0,3}(:|;)]]>\n" +
                    "\t</header>\n" +
                    "<header captGroupNum=\"0\" categories=\"COMMENTS\">\n" +
                    "\t\t<![CDATA[(?i)COMMENTS:]]>\n" +
                    "\t</header>\n" +
                    "<header captGroupNum=\"0\" categories=\"OTHER\">\n" +
                    "\t\t<![CDATA[(?i)ABUSE\\s{1,3}HISTORY\\s{0,3}(:|;)]]>\n" +
                    "\t</header>\n" +
                    "</headers>\n";


    @Test
    public void testUserNameDirectory() {

        assert (Utilities.getUsernameAsDirectory(null).equals(""));
        assert (Utilities.getUsernameAsDirectory("").equals(""));

        assert (Utilities.getUsernameAsDirectory("Mx4DBQ4mODA8JE4pVwAcCgQXPDhL").equals("VHAMASTER-vhaslcornir/"));
    }

    @Test
    public void sectionizerConfigCreatorTest1() {
        assert(Utilities.sectionizerConfigCreator(testString).equals(sectionizerConfigCreatorTest1_Output));
        System.out.println("test.");
    }
}

