package gov.va.vinci.v3nlp.services;

import gov.va.vinci.v3nlp.model.Template;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TemplateServiceImpl implements TemplateService {


    private File templateDirectory;

    @Override
    public Template[] getTemplates() {
    	List<Template> resultList = new ArrayList<Template>();
    	
    
    	for (String dir: templateDirectory.list()) {
    		if (new File(templateDirectory + "/" + dir).isDirectory()) {
    			resultList.addAll(processDirectory(new File(templateDirectory + "/" + dir), dir));
    		}
    	}
        
        Template[] results = new Template[resultList.size()];
        resultList.toArray(results);
        return results;
    }
    
    private List<Template> processDirectory(File d, String groupName) {
    	List<Template> resultList = new ArrayList<Template>();
    	
    	for (File f : d.listFiles(new OnlyExt("v3nlp"))) {
    		Template t = new Template();
    		t.setGroup(groupName);
    		t.setXmlTemplate(readFile(f));
            resultList.add(t);
        }
        return resultList;
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
