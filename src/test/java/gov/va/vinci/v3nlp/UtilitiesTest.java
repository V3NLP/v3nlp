package gov.va.vinci.v3nlp;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class UtilitiesTest {
    private static String SECTIONIZER_CONFIG_CREATE_TEST1_INPUT ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
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

    private static String SECTIONIZER_CONFIG_CREATE_TEST2_INPUT ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
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
            "\t\t<include>COMMENTS</include> " +
            "\t</includes>\n" +
            "\t<excludes>\n" +
            "\t\t<exclude>HIST</exclude>\n" +
            "\t\t<exclude>OTHER</exclude>\n" +
            "\t</excludes>\n" +
            "</headers>";

    private static String SECTIONIZER_CONFIG_CREATE_TEST1_OUTPUT =
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

    private static String SECTIONIZER_CONFIG_CREATE_TEST2_OUTPUT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<headers>\n" +
            "<header captGroupNum=\"0\" categories=\"COMMENTS\">\n" +
            "\t\t<![CDATA[(?i)COMMENTS:]]>\n" +
            "\t</header>\n" +
            "</headers>\n";

    @Test
    public void testUserNameDirectory() {
        assert (Utilities.getUsernameAsDirectory(null).equals(""));
        assert (Utilities.getUsernameAsDirectory("").equals(""));
        assert (Utilities.getUsernameAsDirectory("Mx4DBQ4mODA8JE4pVwAcCgQXPDhL").equals("VHAMASTER-vhaslcornir/"));
    }

    @Test
    public void sectionizerConfigCreatorTest1() throws Exception {
        String result = Utilities.transformDoc(Utilities.sectionizerConfigCreator(SECTIONIZER_CONFIG_CREATE_TEST1_INPUT));
        assert(result.equals(SECTIONIZER_CONFIG_CREATE_TEST1_OUTPUT));
    }

    @Test
    public void sectionizerConfigCreatorTest2() throws Exception {
        String result = Utilities.transformDoc(Utilities.sectionizerConfigCreator(SECTIONIZER_CONFIG_CREATE_TEST2_INPUT));
        assert(result.equals(SECTIONIZER_CONFIG_CREATE_TEST2_OUTPUT));
    }

}

