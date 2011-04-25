package gov.va.vinci.v3nlp.services;

import gov.va.research.v3nlp.common.metamap.MetaMapServiceHttpInvoker;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.model.operations.MetamapConcept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


import org.springframework.web.client.RestTemplate;

public class MetamapProviderServiceImpl implements NlpProcessingUnit {

	private RestTemplate restTemplate;

	private MetaMapServiceHttpInvoker metamapService;

    @Override
    public Corpus process(String config, Corpus c) {
        /**
         *
         * need sections and metamap concept lists.
         */
        List<String> sections =  new ArrayList<String>(); //= dataToProcess.getSectionList();
		MetamapConcept mmc = null; //dataToProcess.getMetamapConcept();
		List<String> semanticGroups = new ArrayList<String>();
		if (mmc.getSemanticGroups() != null
				&& mmc.getSemanticGroups().length > 0) {

			semanticGroups = Arrays.asList(mmc.getSemanticGroups());
		}

		for (DocumentInterface d : c.getDocuments()) {
			if (!sections.isEmpty()) { // Processing sections only.
				List<Annotation> sectionsToBeProcessed = getSectionAnnotations(
						d, sections);

				// Send each section to metamap
				for (Annotation a : sectionsToBeProcessed) {
					// Run Metamap on this section.
					Document newDocument = null;
					try {
						newDocument = metamapService.getMapping(
								d.getContent().substring(a.getBeginOffset(),
										a.getEndOffset()), false,
								new ArrayList<String>(), semanticGroups);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					// Add annotation back in.
					for (AnnotationInterface newAnnotation : newDocument
							.getAnnotations().getAll()) {
						newAnnotation.setBeginOffset(newAnnotation
								.getBeginOffset()
								+ a.getBeginOffset());
						newAnnotation.setEndOffset(newAnnotation.getBeginOffset() + a.getLength() - 1);
						d.getAnnotations().getAll().add(newAnnotation);
					}
				}
			} else { // Processing whole document.
				Document newDocument = null;
				try {
					newDocument = metamapService.getMapping(d.getContent(),
							false, new ArrayList<String>(), semanticGroups);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);

				}
				d.getAnnotations().getAll().addAll(
						newDocument.getAnnotations().getAll());
			}
		}
		return c;
    }

	public gov.va.vinci.cm.Corpus processPipeLine(PipeLine dataToProcess,
			Corpus c) {
		List<String> sections = dataToProcess.getSectionList();
		MetamapConcept mmc = dataToProcess.getMetamapConcept();
		List<String> semanticGroups = new ArrayList<String>();
		if (mmc.getSemanticGroups() != null
				&& mmc.getSemanticGroups().length > 0) {

			semanticGroups = Arrays.asList(mmc.getSemanticGroups());
		}

		for (DocumentInterface d : c.getDocuments()) {
			if (!sections.isEmpty()) { // Processing sections only.
				List<Annotation> sectionsToBeProcessed = getSectionAnnotations(
						d, sections);

				// Send each section to metamap
				for (Annotation a : sectionsToBeProcessed) {
					// Run Metamap on this section.
					Document newDocument = null;
					try {
						newDocument = metamapService.getMapping(
								d.getContent().substring(a.getBeginOffset(),
										a.getEndOffset()), false,
								new ArrayList<String>(), semanticGroups);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					// Add annotation back in.
					for (AnnotationInterface newAnnotation : newDocument
							.getAnnotations().getAll()) {
						newAnnotation.setBeginOffset(newAnnotation
								.getBeginOffset()
								+ a.getBeginOffset());
						newAnnotation.setEndOffset(newAnnotation.getBeginOffset() + a.getLength() - 1);
						d.getAnnotations().getAll().add(newAnnotation);
					}
				}
			} else { // Processing whole document.
				Document newDocument = null;
				try {
					newDocument = metamapService.getMapping(d.getContent(),
							false, new ArrayList<String>(), semanticGroups);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				
				}
				d.getAnnotations().getAll().addAll(
						newDocument.getAnnotations().getAll());
			}
		}
		return c;
	}

	private List<Annotation> getSectionAnnotations(DocumentInterface d,
			List<String> sectionsSelected) {
		FeatureElement sectionFeatureElement = new FeatureElement("type",
				"section_header");
		List<Annotation> annotations = new ArrayList<Annotation>();
		// Walk through annotations
		for (AnnotationInterface a : d.getAnnotations().getAll()) {
			List<String> annotationSections = new ArrayList<String>();
			// Go through all features on an annotation.
			for (Feature f : ((Annotation) a).getFeatures()) {
				if (f.getFeatureElements() == null) {
					continue;
				}

				// See if it is a section element, and if so, get the sections.
				for (FeatureElement fe : f.getFeatureElements()) {
					if (fe.equals(sectionFeatureElement)) {
						annotationSections
								.addAll(getCategoriesFromFeatureElements(f));
					}
				} // End FeatureElement Loop
			} // End Feature Loop

			// See if any of the sections selected are in this annotation. If
			// so, add the
			// annotation to return.
			for (String section : sectionsSelected) {
				if (annotationSections.contains(section)) {
					annotations.add((Annotation) a);
				}
			}
		} // End each Annotation Loop
		return annotations;
	}

	private List<String> getCategoriesFromFeatureElements(Feature f) {
		List<String> returnList = new ArrayList<String>();

		Set<FeatureElement> categories = f
				.getFeatureElementsByName("categories");

		for (FeatureElement categoryElement : categories) {
			String[] cats = ((String)categoryElement.getValue()).split(",");
			for (int i = 0; i < cats.length; i++) {
				returnList.add(cats[i].trim());
			}
		}
		return returnList;
	}

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MetaMapServiceHttpInvoker getMetamapService() {
        return metamapService;
    }

    public void setMetamapService(MetaMapServiceHttpInvoker metamapService) {
        this.metamapService = metamapService;
    }

    @Override
    public void initialize() {
        // No-op in this implementation.
    }

    @Override
    public void destroy() {
        // No-op in this implementation.
    }
}
