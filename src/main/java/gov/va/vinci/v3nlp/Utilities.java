package gov.va.vinci.v3nlp;

import org.apache.commons.validator.GenericValidator;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static String getUsernameAsDirectory(String userToken) {
        if (GenericValidator.isBlankOrNull(userToken)) {
            return "";
        } else {
            return XORCipher.decode(userToken).replace("\\", "-") + "/";
        }
    }

    public static String sectionizerConfigCreator(String rawConfig) {
        org.w3c.dom.Document doc = null;
        try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            // Create the builder and parse the file
            doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(rawConfig)));
            if (doc == null) {
                throw new RuntimeException("Custom sectionizer rules parsing returned null.");
            }

            DocumentImpl  resultDocument = new DocumentImpl();
            Element resultRoot = resultDocument.createElement("headers");
            resultDocument.appendChild(resultRoot);

            NodeList headerRecords = doc.getElementsByTagName("header");
            NodeList inclusions = doc.getElementsByTagName("include");
            NodeList exclusions = doc.getElementsByTagName("exclude");


            /**
             * Parse inclusion/exclusion lists.
             */
            List<String> inclusionList = new ArrayList<String>();
            for (int i=0; i < inclusions.getLength(); i++) {
                inclusionList.add(inclusions.item(i).getTextContent());
            }
            List<String> exclusionList = new ArrayList<String>();
            for (int i=0; i < exclusions.getLength(); i++) {
                exclusionList.add(exclusions.item(i).getTextContent());
            }

            /**
             * Build an appropriate configuration file from the header config and inclusion/exclusion list.
             */
            for (int i=0; i < headerRecords.getLength(); i++) {
                Node n = headerRecords.item(i);
                String headerCategories = n.getAttributes().getNamedItem("categories").getNodeValue();
                String[] eachCat = headerCategories.split(",");

                String newCategories = "";
                for (String currentNodeCat: eachCat) {
                    if (exclusionList.contains(currentNodeCat)) {
                        System.out.println("Need to do exclusion.");
                    } else if (inclusionList.size() > 0 && !inclusionList.contains(currentNodeCat)) {
                        System.out.println("Not in inclusion list, and inclusion list is populated...");
                    } else {
                        newCategories += currentNodeCat + ", ";
                    }
                }

                // Only add the node to the new config if it has any categories.
                if (newCategories.trim().length() > 1) {
                    n.getAttributes().getNamedItem("categories").setNodeValue(newCategories.substring(0, newCategories.length() - 2));
                    resultRoot.appendChild(resultDocument.importNode(n, true));
                }
            }


            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(resultDocument);
            transformer.transform(source, result);

            return result.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
