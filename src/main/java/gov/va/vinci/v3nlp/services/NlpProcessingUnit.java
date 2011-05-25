package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;

public interface NlpProcessingUnit {

    public abstract DocumentInterface process(String config, DocumentInterface _document);

    public abstract void initialize();

    public abstract void destroy();
}
