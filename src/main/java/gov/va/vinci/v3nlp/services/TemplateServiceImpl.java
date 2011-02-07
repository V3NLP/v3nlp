package gov.va.vinci.v3nlp.services;

import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TemplateServiceImpl implements TemplateService {


    private File templateDirectory;

    @Override
    public String[] getTemplates() {
    	List<String> resultList = new ArrayList<String>();
    	
        for (File f : templateDirectory.listFiles(new OnlyExt("v3nlp"))) {
            resultList.add(readFile(f));
        }
        
        String[] results = new String[resultList.size()];
        resultList.toArray(results);
        return results;
    }

    @Required
    public void setTemplateDirectory(String templateDirectory) {
        this.templateDirectory = new File(templateDirectory);
        if (!this.templateDirectory.isDirectory()) {
            throw new RuntimeException(templateDirectory + " is not a directory.");
        }
    }

    private String readFile(File f) {
    	StringBuffer toReturn = new StringBuffer();
    	try {
    	    BufferedReader in = new BufferedReader(new FileReader(f));
    	    String str;
    	    while ((str = in.readLine()) != null) {
    	    	toReturn.append(str);
    	    }
    	    in.close();
    	} catch (IOException e) {
    	}
    	return toReturn.toString();
    }

    class OnlyExt implements FilenameFilter {
        String ext;

        public OnlyExt(String ext) {
            this.ext = "." + ext;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }
}
