package gov.va.research.inlp;

import gov.va.research.inlp.model.BaseNlpModule;
import gov.va.research.inlp.model.PipeLine;
import gov.va.research.inlp.model.datasources.DataServiceSource;
import gov.va.research.inlp.services.DatabaseRepositoryService;
import gov.va.research.inlp.services.MetamapProviderServiceImpl;
import gov.va.research.inlp.services.NegationImpl;
import gov.va.research.inlp.services.SectionizerAndConceptFinderImpl;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class DefaultNlpServiceImpl implements NlpService {

	@Getter
	@Setter
	SectionizerAndConceptFinderImpl sectionizerAndConceptFinder = null;

	@Getter
	@Setter
	private List<String> annotationTypesToReturn = new ArrayList<String>();

	@Getter
	@Setter
	private MetamapProviderServiceImpl metamapProvider;

	@Getter
	@Setter
	private NegationImpl negationProvider;

	@Getter
	@Setter
	private DatabaseRepositoryService databaseRepositoryService;

	@Override
	public List<String> getAvailableSectionHeaders() {
		return sectionizerAndConceptFinder.getAvailableSectionHeaders();
	}

	@Override
	public String getDefaultNegationConfiguration() throws Exception {
		StringBuffer sb = new StringBuffer();
		for (String s : negationProvider.getDefaultNegationConfiguration()) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String getDefaultSectionizerConfiguration() throws Exception {
		return sectionizerAndConceptFinder.getDefaultSectionizerConfiguration();
	}

	@Override
	public Corpus processPipeLine(PipeLine dataToProcess, Corpus corpus) {
		Corpus returnCorpus = corpus;

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

			// Step 1 - Sections and Concepts go first through annotation.
			if (dataToProcess.hasSectionCriteria()
					|| dataToProcess.hasConcept()) {
				returnCorpus = sectionizerAndConceptFinder.processPipeLine(
						dataToProcess, returnCorpus);
			}

			// Step 2 - Handle metamap processing.
			if (dataToProcess.getMetamapConcept() != null) {
				try {
				returnCorpus = metamapProvider.processPipeLine(dataToProcess,
						returnCorpus);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			// Remmove annotations not in return list before potentially sending
			// to Negation.
			Corpus finalCorpus = removeUnneededAnnotations(returnCorpus);

			// Negation -- Right now we are putting negation at the end of the
			// processing.
			// This will likely change in the next release, but if we change to
			// UIMA, this
			// will change anyway, so was left as the last module for
			// simplicity.
			// @TODO Add negation
			if (dataToProcess.getNegation() != null) {
				this.negationProvider.process(finalCorpus, dataToProcess
						.getNegation());
			}

			/** Add the format tags that were passed through. **/
			for (int d = 0; d < dataToProcess.getServices().size(); d++) {
				if (dataToProcess.getServices().get(d).getFormatInfo() != null) {
					finalCorpus.addFormatInfo(dataToProcess.getServices()
							.get(d).getFormatInfo());
				}
			}
			return finalCorpus;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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