package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;

public interface NlpProcessingUnit {

    public abstract gov.va.vinci.cm.Corpus process(String config, Corpus _corpus);

    public abstract void initialize();

    public abstract void destroy();
}
