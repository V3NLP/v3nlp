package gov.va.vinci.v3nlp.model;

import gov.va.vinci.cm.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CorpusSummary implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Corpus corpus;

    private List<String> pedigrees = new ArrayList<String>();

    private List<DocumentPedigreeCount> documentPedigreeCounts = new ArrayList<DocumentPedigreeCount>();


    /**
     * Full arg constructor for corpus args, needed to copy a corpus in.
     *
     * @param documents
     * @param directoryName
     * @param documentNames
     * @param formatInfoFile
     * @param corpusName
     * @param formatInfos
     */
    public CorpusSummary(Corpus c) {
        this.setCorpus(c);
    }

    public CorpusSummary() {
    }

    public void setCorpus(Corpus c) {
        this.corpus = c;
        updateSummary();
    }

    public void updateSummary() {
        pedigrees.clear();
        documentPedigreeCounts.clear();

        for (DocumentInterface doc : corpus.getDocuments()) {
            HashMap<String, Long> tempResults = new HashMap<String, Long>();
            for (AnnotationInterface a : doc.getAnnotations().getAll()) {
                if (a instanceof Annotation) {
                    for (Feature f : ((Annotation) a).getFeatures()) {
                        if (f.getMetaData() == null || f.getMetaData().getPedigree() == null) {
                            continue;
                        }

                        String tempPedigree = f.getMetaData().getPedigree();
                        if (!pedigrees.contains(tempPedigree)) {
                            pedigrees.add(tempPedigree);
                        }
                        if (tempResults.containsKey(tempPedigree)) {
                            tempResults.put(tempPedigree, ((Long) tempResults.get(tempPedigree)).longValue() + 1L);
                        } else {
                            tempResults.put(tempPedigree, 1L);
                        }
                    }
                }

            } // End For Annotations

            for (String key : tempResults.keySet()) {
                this.documentPedigreeCounts.add(new DocumentPedigreeCount(doc.getDocumentId(), key, tempResults.get(key)));
            }
        } // End for each document
    }

    public List<String> getPedigrees() {
        return pedigrees;
    }

    public void setPedigrees(List<String> pedigrees) {
        this.pedigrees = pedigrees;
    }

    public List<DocumentPedigreeCount> getDocumentPedigreeCounts() {
        return documentPedigreeCounts;
    }

    public void setDocumentPedigreeCounts(List<DocumentPedigreeCount> documentPedigreeCounts) {
        this.documentPedigreeCounts = documentPedigreeCounts;
    }

    public Corpus getCorpus() {
        return corpus;
    }
}

