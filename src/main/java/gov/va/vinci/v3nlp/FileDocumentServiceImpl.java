/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileDocumentServiceImpl implements DocumentService {

    @Override
    public List<DocumentInterface> getRandomDocuments(Integer numberToGet) {
        BufferedReader reader = null;
        try {
            List<DocumentInterface> docs = new ArrayList<DocumentInterface>();


            if (numberToGet > 6) {
                throw new RuntimeException(
                        "This document service can only return 6 documents maximium.");
            }

            if (numberToGet < 1) {
                return docs;
            }

            for (int i = 0; i < numberToGet; i++) {
                Resource r = new ClassPathResource("exampledata/" + i + ".txt");


                reader = new BufferedReader(new FileReader(r.getFile()));
                String text = null;
                StringBuffer docContent = new StringBuffer();

                while ((text = reader.readLine()) != null) {
                    docContent.append(text);
                }

                DocumentInterface d = new Document();
                d.setDocumentId("" + i);
                d.setDocumentName("FileDocumentService." + i);
                d.setContent(text);
                docs.add(d);
            }
            return docs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
}
