/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.uima;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.flap.cr.Analyte;
import gov.va.vinci.flap.cr.SubReader;

import java.util.HashMap;
import java.util.logging.Logger;


public class CorpusSubReader implements SubReader {
    Corpus corpus = null;
    int currentIndex = 0;
    private static Logger log = Logger.getLogger(CorpusSubReader.class.getCanonicalName()) ;

    public CorpusSubReader() {

    }

    public CorpusSubReader(Corpus _c) {
        this.corpus = _c;
    }

    @Override
    public void initialize(HashMap<String, String> stringStringHashMap) {

    }

    @Override
    public int getCollectionSize() {
        if (corpus == null || corpus.getDocuments() == null) {
            return 0;
        } else {
            return corpus.getDocuments().size();
        }
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasNext() {
        return (currentIndex < corpus.getDocuments().size() );
    }

    @Override
    public Analyte getNext() throws Exception {
        DocumentInterface d = corpus.getDocuments().get(currentIndex);
        currentIndex++;
        return new Analyte(d.getDocumentId(), d.getDocumentName(), d.getContent());
    }

    @Override
    public void close() throws Exception {
        // No-op
    }
}
