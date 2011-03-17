package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;
import gov.va.vinci.v3nlp.model.BaseNlpModule;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import gov.va.vinci.v3nlp.model.operations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.scheduling.annotation.Async;

public class PipeLineProcessorImpl implements PipeLineProcessor {

    private String directoryToStoreResults;

    private DatabaseRepositoryService databaseRepositoryService;

    /**
     * **********************************************************
     * Services For NLP
     * ***********************************************************
     */

    /* Concept */
    private ConceptFinderService conceptFinderService;

    /* MetamapConcept */
    private MetamapProviderServiceImpl metamapProvider;

    /* Negation */
    private NegationImpl negationService;

    /* OParser */
    private OParserService oParserService;

    /* PosTagger */
    private POSTaggerService posTaggerService;

    /* Sectionizer */
    private SectionizerService sectionizerService;

    /* SentenceSplitterService */
    private SentenceSplitterService sentenceSplitterService;

    private TokenizerService tokenizerService;

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

    @Override
    @Async
    public void processPipeLine(String pipeLineId, PipeLine dataToProcess,
                                Corpus corpus) {
        Corpus returnCorpus = corpus;

        System.out.println("Begin pipeline processing [" + dataToProcess.getPipeLineName() + "] at " + new Date());
        try {
            // Step 0 - Add documents from chosen data source.
            for (BaseNlpModule m : dataToProcess.getServices()) {
                if (m instanceof DataServiceSource) {
                    // Get data and add to corpus for this dataServiceSource.
                    List<DocumentInterface> rdocs = databaseRepositoryService
                            .getDocuments((DataServiceSource) m);
                    for (DocumentInterface doc : rdocs) {
                        returnCorpus.addDocument(doc);
                    }
                }
            }

            for (BaseNlpModule m : dataToProcess.getServices()) {
                if (m instanceof DataServiceSource) {
                    continue;
                }

                System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
                        + m.getClass().getName() + " at " + new Date());

                if (m instanceof Concept) {
                    returnCorpus = this.conceptFinderService.conceptFinder(dataToProcess.getRegularExpressionConfiguration(), dataToProcess.getSectionCriteriaExpression(), returnCorpus);
                } else if (m instanceof MetamapConcept) {
                    returnCorpus = this.metamapProvider.processPipeLine(dataToProcess, returnCorpus);
                } else if (m instanceof Negation) {
                    returnCorpus = this.negationService.process(returnCorpus, dataToProcess.getNegation());
                } else if (m instanceof OParser) {
                    returnCorpus = this.oParserService.parse(returnCorpus);
                } else if (m instanceof PosTagger) {
                    returnCorpus = this.posTaggerService.posTagging(returnCorpus);
                } else if (m instanceof Sectionizer) {
                    returnCorpus = this.sectionizerService.sectionize(dataToProcess.getCustomSectionRules(), returnCorpus);
                } else if (m instanceof SentenceSplitter) {
                    returnCorpus = this.sentenceSplitterService.splitSentences(returnCorpus);
                } else if (m instanceof Tokenizer) {
                    returnCorpus = tokenizerService.tokenize(returnCorpus);
                }
            }

            CorpusSummary finalCorpus = new CorpusSummary(removeUnneededAnnotations(returnCorpus, dataToProcess));

            System.out.println("End pipeline processing [" + dataToProcess.getPipeLineName() + "] at " + new Date());

            serializeObject(this.directoryToStoreResults + pipeLineId
                    + ".results", finalCorpus);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            serializeObject(this.directoryToStoreResults + pipeLineId + ".err",
                    e);
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

    public void setConceptFinderService(ConceptFinderService conceptFinderService) {
        this.conceptFinderService = conceptFinderService;
    }

    public void setMetamapProvider(MetamapProviderServiceImpl metamapProvider) {
        this.metamapProvider = metamapProvider;
    }

    public void setNegationService(NegationImpl negationService) {
        this.negationService = negationService;
    }

    public void setoParserService(OParserService oParserService) {
        this.oParserService = oParserService;
    }

    public void setPosTaggerService(POSTaggerService posTaggerService) {
        this.posTaggerService = posTaggerService;
    }

    public void setSectionizerService(SectionizerService sectionizerService) {
        this.sectionizerService = sectionizerService;
    }

    public void setSentenceSplitterService(SentenceSplitterService sentenceSplitterService) {
        this.sentenceSplitterService = sentenceSplitterService;
    }

    public void setTokenizerService(TokenizerService tokenizerService) {
        this.tokenizerService = tokenizerService;
    }

}
