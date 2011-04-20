package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;

public interface NlpProcessingUnit {
    public gov.va.vinci.cm.Corpus execute(String config, Corpus _corpus);

}
