package gov.va.vinci.v3nlp.services;


import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;

import java.util.ArrayList;
import java.util.List;

public class BaseNlpProcessingUnit implements NlpProcessingUnit{

    protected List<Annotation> getProcessList(DocumentInterface originalDocument, List<NlpComponent> previousModuleProvided) {
        List<Annotation> toProcess = new ArrayList<Annotation>();
        if (previousModuleProvided == null || previousModuleProvided.size() < 1) {
            Annotation a = new Annotation();
            a.setBeginOffset(0);
            a.setEndOffset(originalDocument.getContent().length() - 1);
            a.setContent(originalDocument.getContent());
            toProcess.add(a);
        } else {
            for (NlpComponent p : previousModuleProvided) {
                toProcess.addAll(((gov.va.vinci.cm.Document) originalDocument).findAnnotationsWithPedigree(p.getPedigree()));
            }
        }
        return toProcess;
    }

    @Override
    public DocumentInterface process(String config, DocumentInterface _document, List<NlpComponent> previousModuleProvided) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initialize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
