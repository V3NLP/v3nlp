package gov.va.research.inlp;
import gov.va.research.inlp.model.PipeLine;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

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
	
	@Override
	public List<String> getAvailableSectionHeaders() {
		return sectionizerAndConceptFinder.getAvailableSectionHeaders();
	}

	@Override
	public String getDefaultNegationConfiguration() throws Exception {
		return sectionizerAndConceptFinder.getDefaultNegationConfiguration();
	}

	@Override
	public String getDefaultSectionizerConfiguration() throws Exception {
		return sectionizerAndConceptFinder.getDefaultSectionizerConfiguration();
	}

	@Override
	public Corpus processPipeLine(PipeLine dataToProcess) {
		Corpus returnCorpus = null;

		// Step 1 - Sections and Concepts go first through annotation. 
		if (dataToProcess.hasSectionCriteria() || dataToProcess.hasConcept())
		{
			returnCorpus = sectionizerAndConceptFinder.processPipeLine(dataToProcess);
		}
		
		// Step 2 - Handle metamap processing. 
		if (dataToProcess.getMetamapConcept() != null) {
			
			// TODO Handle Metamap processing. 
		}
		
		// Remmove annotations not in return list & return annotated corpus. 
		return removeUnneededAnnotations(returnCorpus);
	}
	
	
	/**
	 * Iterate through the annotation on the documents in the corpus and remove those
	 * that are not listed in annotationTypesToReturn (as Annotation.Feature.featureLabel="type", value=in annotationTypeToReturn list)
	 * @param c
	 * @return The corpus with non-needed annotations removed. 
	 */
	private Corpus removeUnneededAnnotations(Corpus c)
	{
		for (DocumentInterface d: c.getDocuments()) {
			List<AnnotationInterface> anns = d.getAnnotations().getAll();
			boolean keep = false;
			for (int i = anns.size() - 1;   i >= 0; i--) {
				keep = false;
				Annotation a = (Annotation)anns.get(i);
				for (Feature f: a.getFeatures()) {
					for (FeatureElement fe: f.getFeatureElements()) {
						if (fe.getName() != null && fe.getName().equals("type")) {
							if (this.annotationTypesToReturn.contains(fe.getValue())) {
								keep = true;
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