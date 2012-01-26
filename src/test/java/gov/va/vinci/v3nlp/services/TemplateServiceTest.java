/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TemplateServiceTest {
    File directory = new File(".");
    TemplateServiceImpl service = new TemplateServiceImpl();

    public TemplateServiceTest() {
        try {
            service.setTemplateDirectory(directory.getCanonicalPath() + "/src/test/v3nlp-templates");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Test
    public void testGetTemplates() {
        assert(service.getTemplates().length == 12);
    }
    
    @Test
    public void testBadDirectory() {
        TemplateServiceImpl bad = new TemplateServiceImpl();
        try {
            bad.setTemplateDirectory("C:/junk-test-does-not-exits-sdfdsfdsfsd");
            Assert.fail("Should have failed on bad directory.");
        }catch (Exception e) {

        }
    }
}
