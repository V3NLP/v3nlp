package gov.va.research.inlp.services;

import gov.va.research.inlp.model.BaseNlpModule;
import gov.va.research.inlp.model.CorpusSummary;
import gov.va.research.inlp.model.PipeLine;
import gov.va.research.inlp.model.datasources.DataServiceSource;
import gov.va.research.inlp.model.operations.Concept;
import gov.va.research.inlp.model.operations.MetamapConcept;
import gov.va.research.inlp.model.operations.Negation;
import gov.va.research.inlp.model.operations.OParser;
import gov.va.research.inlp.model.operations.PosTagger;
import gov.va.research.inlp.model.operations.Sectionizer;
import gov.va.research.inlp.model.operations.SentenceSplitter;
import gov.va.research.inlp.model.operations.Tokenizer;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Setter;

import org.springframework.scheduling.annotation.Async;

public class PipeLineProcessorImpl {

	@Setter
	private String directoryToStoreResults;

	@Setter
	private DatabaseRepositoryService databaseRepositoryService;

	/*************************************************************
	 * Services For NLP
	 *************************************************************/
	
	/* Concept */
	@Setter
	private ConceptFinderService conceptFinderService;
	
	/* MetamapConcept */
	@Setter
	private MetamapProviderServiceImpl metamapProvider;
	
	/* Negation */
	@Setter
	private NegationImpl negationService;
	
	/* OParser */
	@Setter
	private OParserService oParserService;
	
	/* PosTagger */
	@Setter
	private POSTaggerService posTaggerService;

	/* Sectionizer */
	@Setter
	private SectionizerService sectionizerService;
	
	/* SentenceSplitterService */
	@Setter
	private SentenceSplitterService sentenceSplitterService;
	
	@Setter
	private TokenizerService tokenizerService;

	/* TokenizerService */
	
	/************************************************************
	 * End NLP Services
	 ************************************************************/


	@Setter
	private List<String> annotationTypesToReturn = new ArrayList<String>();



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
		Corpus returnCorpus = corpus;
		
		System.out.println("Begin pipeline processing [" + dataToProcess.getPipeLineName() + "] at " + new Date());
		try {
			// Step 0 - Add documents from chosen datasource.
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
				} else if (m instanceof Concept) {
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.conceptFinderService.conceptFinder(dataToProcess.getRegularExpressionConfiguration(), dataToProcess.getSectionCriteriaExpression(), returnCorpus);
			
				} else if (m instanceof MetamapConcept) {
			
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.metamapProvider.processPipeLine(dataToProcess, returnCorpus);
					
				} else if (m instanceof Negation) {
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.negationService.process(returnCorpus, dataToProcess
						.getNegation());
					
				} else if (m instanceof OParser) {
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.oParserService.parse(returnCorpus);

				} else if (m instanceof PosTagger) {
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.posTaggerService.posTagging(returnCorpus);
					
				} else if (m instanceof Sectionizer) {
				
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.sectionizerService.sectionize( dataToProcess.getCustomSectionRules(), returnCorpus);
					
				} else if (m instanceof SentenceSplitter) {
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = this.sentenceSplitterService.splitSentences(returnCorpus);
					
				} else if (m instanceof Tokenizer) { 
					
					System.out.println("-> [" + dataToProcess.getPipeLineName() + "] Running modules for "
							+ m.getClass().getName());
					returnCorpus = tokenizerService.tokenize(returnCorpus);
			
				}
			}
			


			// Remmove annotations not in return list before potentially sending
			// to Negation.
			CorpusSummary finalCorpus = new CorpusSummary(returnCorpus); // removeUnneededAnnotations(returnCorpus);


			/** Add the format tags that were passed through. **/
			for (int d = 0; d < dataToProcess.getServices().size(); d++) {
				if (dataToProcess.getServices().get(d).getFormatInfo() != null) {
					finalCorpus.getCorpus().addFormatInfo(dataToProcess.getServices()
							.get(d).getFormatInfo());
				}
			}

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
	private Corpus removeUnneededAnnotations(Corpus c) {
		for (DocumentInterface d : c.getDocuments()) {
			List<AnnotationInterface> anns = d.getAnnotations().getAll();
			boolean keep = false;
			for (int i = anns.size() - 1; i >= 0; i--) {
				keep = false;
				Annotation a = (Annotation) anns.get(i);
				if (a.getFeatures() != null) {
					for (Feature f : a.getFeatures()) {
						for (FeatureElement fe : f.getFeatureElements()) {
							System.out.println(fe.getName());
							if ("type".equals(fe.getName())) {
								if (this.annotationTypesToReturn.contains(fe
										.getValue())) {
									keep = true;
								}
							}
						}
					}
				}
				if (!keep) {
					anns.remove(i);
				}

			}
		}
		return c;
	}
}
