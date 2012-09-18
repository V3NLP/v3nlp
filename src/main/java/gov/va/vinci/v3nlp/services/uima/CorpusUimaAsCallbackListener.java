/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.uima;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import gov.va.vinci.nlp.framework.marshallers.commonModel.util.ToCommonModel;

import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;

import java.util.logging.Logger;


public class CorpusUimaAsCallbackListener extends UimaAsBaseCallbackListener {
    private static Logger log = Logger.getLogger(CorpusUimaAsCallbackListener.class.getCanonicalName());

    private Corpus corpus = new Corpus();
    private ToCommonModel toCommonModel = new ToCommonModel();

    public CorpusUimaAsCallbackListener(Corpus c) {

    }

    @Override
    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {

        log.info("===== Got CAS BACK : " + aCas);
        try {
            Document d = toCommonModel.convert(aCas.getJCas());
            corpus.getDocuments().add(d);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void collectionProcessComplete(EntityProcessStatus aStatus) {
        log.info("===== Processing complete.");
    }//collectionProcessComplete method

    public Corpus getCorpus() {
        return this.corpus;
    }

}
