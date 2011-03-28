package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.model.BaseNlpModule;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.model.operations.BaseOperation;
import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.text.AnnotationIndex;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.*;

public class UIMAPipeLineProcessorImpl implements PipeLineProcessor {

    private String directoryToStoreResults;

    private DatabaseRepositoryService databaseRepositoryService;

    public static List<String> FEATURES_TO_IGNORE = new ArrayList<String>();

    {
        FEATURES_TO_IGNORE.add("uima.tcas.Annotation:begin");
        FEATURES_TO_IGNORE.add("uima.tcas.Annotation:end");
        FEATURES_TO_IGNORE.add("uima.cas.AnnotationBase:sofa");
    }

    ;

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
        UimaAsynchronousEngine uimaAsEngine2 = new BaseUIMAAsynchronousEngine_impl();

        Map<String, Object> appCtx = new HashMap<String, Object>();


        try {
            appCtx = new HashMap<String, Object>();
            appCtx.put(UimaAsynchronousEngine.ServerUri, "tcp://Ryan-Cornias-MacBook-Pro.local:61616");
            appCtx.put(UimaAsynchronousEngine.Endpoint, "TokenizerSimple"); //"SentenceTokenizerSimple");
            appCtx.put(UimaAsynchronousEngine.CasPoolSize, 2);

            CAS cas;
            Corpus c = new Corpus();

            for (int i = 0; i < corpus.getDocuments().size(); i++) {
                DocumentInterface currDoc = corpus.getDocuments().get(i);

                uimaAsEngine2 = new BaseUIMAAsynchronousEngine_impl();
                uimaAsEngine2.initialize(appCtx);

                cas = uimaAsEngine2.getCAS();

                cas.setDocumentText(currDoc.getContent());
                System.out.println("Sending for processing!!!!");
                String result = uimaAsEngine2.sendAndReceiveCAS(cas);
                Document d = convertCasToDocument(cas);
                d.setDocumentName(currDoc.getDocumentName());
                d.setDocumentId(currDoc.getDocumentId());
                c.addDocument(d);
                uimaAsEngine2.stop();

            }



            /**
             FileOutputStream out = null;
             try {
             File xmiOutFile = new File(this.directoryToStoreResults, pipeLineId + ".results");
             out = new FileOutputStream(xmiOutFile);
             XmiCasSerializer.serialize(cas, out);
             } catch (Exception e) {
             e.getStackTrace().toString();
             } finally {
             try {
             if (out != null) out.close();
             } catch (Exception e) {  }
             }//finally        **/



            serializeObject(this.directoryToStoreResults + pipeLineId + ".results", new CorpusSummary(c));
            System.out.println("Serialized to:" + this.directoryToStoreResults + pipeLineId + ".results");

        } catch (Exception e) {
            System.out.println("Got Exception!!!!" + e);
            e.printStackTrace();
            serializeObject(this.directoryToStoreResults + pipeLineId + ".err", e);
        } finally {
            System.out.println("Deleting lock...");
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

    public Document convertCasToDocument(CAS cas) {
        System.out.println("Starting conversion:" + new Date() + " -- " + new Date().getTime());
        Document d = new Document();
        d.setContent(cas.getDocumentText());

        AnnotationIndex ai = cas.getAnnotationIndex();
        Iterator it = ai.iterator();
        Map<String, Annotation> annotationMap = new HashMap<String, Annotation>();

        while (it.hasNext()) {
            FeatureStructure fs = (FeatureStructure) it.next();
            int begin = fs.getIntValue(fs.getType().getFeatureByBaseName("begin"));
            int end = fs.getIntValue(fs.getType().getFeatureByBaseName("end"));

            Annotation a = annotationMap.get(begin + "-" + end);
            if (a == null) {
                // No annotation for this span, create a new one.
                a = new Annotation();
                a.setBeginOffset(begin);
                a.setEndOffset(end);
            }


            //   int id = fs.getIntValue(fs.getType().getFeatureByBaseName("id"));

            Feature f = new Feature();

            /** Set Metadata **/
            FeatureMetaData fmd = new FeatureMetaData();
            fmd.setCreatedDate(new Date());
            fmd.setAuthor("keywords");
            fmd.setPedigree(fs.getType().getName());
            f.setMetaData(fmd);

           // System.out.println("TYPE=" + fs.getType() + " >>>> " + "start/end:" + begin + "/" + end);
            List<org.apache.uima.cas.Feature> features = fs.getType().getFeatures();

            /** Add Feature Elements **/
            Set<FeatureElement> fes = new HashSet<FeatureElement>();

            // Copy over features.
            for (org.apache.uima.cas.Feature feat : features) {
                if (FEATURES_TO_IGNORE.contains(feat.getName())) {
                    continue;
                }
                FeatureElement fe = new FeatureElement();
                fe.setName(feat.getShortName());
                fe.setValue(fs.getFeatureValueAsString(feat));


              //  System.out.println("\t\tFeature:" + feat + "--" + feat.getShortName() + " -- " + "Value: " + fs.getFeatureValueAsString(feat) + "-- Range:" + feat.getRange());

            }
            annotationMap.put(begin + "-" + end, a);
            a.addFeature(f);
            d.addAnnotation(a);
        }

        System.out.println("End conversion:" + new Date() + " -- " + new Date().getTime());
        return d;
    }

    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

    public void setDatabaseRepositoryService(DatabaseRepositoryService databaseRepositoryService) {
        this.databaseRepositoryService = databaseRepositoryService;
    }


}
