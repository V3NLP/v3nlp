package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.model.BaseNlpModule;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.model.operations.BaseOperation;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIMAPipeLineProcessorImpl implements PipeLineProcessor {

    private String directoryToStoreResults;

    private DatabaseRepositoryService databaseRepositoryService;

    /**
     * **********************************************************
     * Services For NLP
     * ***********************************************************
     */



    /* TokenizerService */

    /**
     * *********************************************************
     * End NLP Services
     * **********************************************************
     */

    public void init() {

        if (!new File(directoryToStoreResults).exists()
                || !new File(directoryToStoreResults).isDirectory()) {
            throw new RuntimeException(directoryToStoreResults
                    + ": Is not a valid directory to store results.");
        }
    }

    @Async
    public void processPipeLine(String pipeLineId, PipeLine dataToProcess,
                                Corpus corpus) {
        // TODO Implement via UIMA

        UimaAsynchronousEngine uimaAsEngine2 = new BaseUIMAAsynchronousEngine_impl();

        Map<String, Object> appCtx = new HashMap<String, Object>();


        try {
            CAS cas;
            appCtx = new HashMap<String, Object>();
            appCtx.put(UimaAsynchronousEngine.ServerUri, "tcp://Ryan-Cornias-MacBook-Pro.local:61616");
            appCtx.put(UimaAsynchronousEngine.Endpoint, "TokenizerSimple"); //"SentenceTokenizerSimple");
            appCtx.put(UimaAsynchronousEngine.CasPoolSize, 2);
            uimaAsEngine2.initialize(appCtx);
            cas = uimaAsEngine2.getCAS();


            cas.setDocumentText("This is a test. This is only a test. ");
            String result = uimaAsEngine2.sendAndReceiveCAS(cas);

            System.out.println("Result=" + result);


            //   XmiCasSerializer ser = new XmiCasSerializer(new TypeSystem(uimaAsEngine2.getMetaData().getTypeSystem()));
            //   String xml =

            FileOutputStream out = null;
            try {
                File xmiOutFile = new File(this.directoryToStoreResults , pipeLineId + ".results");
                out = new FileOutputStream(xmiOutFile);
                XmiCasSerializer.serialize(cas, out);
            } catch (Exception e) {
                e.getStackTrace().toString();
            } finally {
                try {
                    if (out != null) out.close();
                } catch (Exception e) { /* Don't care if there is an exception closing */ }
            }//finally


            // serializeObject(this.directoryToStoreResults + pipeLineId
            //         + ".results", cas);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            serializeObject(this.directoryToStoreResults + pipeLineId + ".err", e);
        } finally {
            new File(directoryToStoreResults + pipeLineId + ".lck").delete();
        }

    }

    private void serializeObject(String path, Object e) {
        OutputStream os;
        try {
            os = new FileOutputStream(path);
            ObjectOutput oo = new ObjectOutputStream(os);
            oo.writeObject(e);
            oo.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Iterate through the annotation on the documents in the corpus and remove
     * those that are not listed in annotationTypesToReturn (as
     * Annotation.Feature.featureLabel="type", value=in annotationTypeToReturn
     * list)
     *
     * @param c
     * @return The corpus with non-needed annotations removed.
     */
    private Corpus removeUnneededAnnotations(Corpus c, PipeLine p) {
        List<String> toRemove = new ArrayList<String>();

        for (BaseNlpModule m : p.getServices()) {
            if (m instanceof BaseOperation && !((BaseOperation) m).isKeepFeatureInResult()) {
                toRemove.addAll(((BaseOperation) m).getProvides());
            }
        }

        for (DocumentInterface d : c.getDocuments()) {
            List<AnnotationInterface> anns = d.getAnnotations().getAll();
            boolean keep = false;
            for (int i = anns.size() - 1; i >= 0; i--) {
                keep = false;
                Annotation a = (Annotation) anns.get(i);
                List<Feature> toBeRemoved = new ArrayList<Feature>();
                if (a.getFeatures() != null) {
                    for (Feature f : a.getFeatures()) {
                        if (f.getMetaData() != null && f.getMetaData().getPedigree() != null) {
                            if (toRemove.contains(f.getMetaData().getPedigree())) {
                                toBeRemoved.add(f);
                            }
                        }
                    }
                    a.getFeatures().removeAll(toBeRemoved);
                    if (a.getFeatures().size() == 0) {
                        anns.remove(a);
                    }

                }
            }

        }
        return c;
    }

    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

    public void setDatabaseRepositoryService(DatabaseRepositoryService databaseRepositoryService) {
        this.databaseRepositoryService = databaseRepositoryService;
    }



}
