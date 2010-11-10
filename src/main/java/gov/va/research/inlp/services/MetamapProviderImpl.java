package gov.va.research.inlp.services;

import gov.va.research.inlp.model.PipeLine;
import gov.va.research.v3nlp.common.metamap.MetaMapServiceHttpInvoker;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.AnnotationsInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import org.springframework.web.client.RestTemplate;

public class MetamapProviderImpl {

	@Setter
	@Getter
	private RestTemplate restTemplate;

	@Setter
	@Getter
	private MetaMapServiceHttpInvoker metamapService;

	@SneakyThrows
	public gov.va.vinci.cm.Corpus processPipeLine(PipeLine dataToProcess,
			Corpus c) {
		List<String> sections = dataToProcess.getSectionList();

		for (DocumentInterface d : c.getDocuments()) {
			if (!sections.isEmpty()) { // Processing sections only.
				List<Annotation> sectionAnnotations = getSectionAnnotations(d,
						sections);
				System.out.println("Sections that need processed: " + sectionAnnotations.size());
				// TODO
				// Send each section to metamap
				// Add annotation back in.

			} else { // Processing whole document.
				Document newDocument = metamapService.getMapping(
						d.getContent(), false, new ArrayList<String>(),
						new ArrayList<String>());
				AnnotationsInterface metamapAnnotations = newDocument
						.getAnnotations();
				System.out.println("	d.getAnnotations()" + 	d.getAnnotations());
				d.getAnnotations().getAll().addAll(metamapAnnotations.getAll());
				System.out.println("Metamap annotations for this documents: "
						+ metamapAnnotations.getAll().size());
			}
		}
		return c;
	}

	private List<Annotation> getSectionAnnotations(DocumentInterface d,
			List<String> sectionsSelected) {
		FeatureElement sectionFeatureElement = new FeatureElement("type",
				"section_header");
		List<Annotation> features = new ArrayList<Annotation>();
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
						annotationSections.addAll(getCategoriesFromFeatureElements(f));
					}
				} // End FeatureElement Loop
			} // End Feature Loop
			
			// See if any of the sections selected are in this annotation. If so, add the 
			// annotation to return. 
			for (String section: sectionsSelected) 
			{
				if (annotationSections.contains(section)) {
					features.add((Annotation)a);
				}
			}
		} // End each Annotation Loop
		return features;
	}

	private List<String> getCategoriesFromFeatureElements(Feature f) {
		List<String> returnList = new ArrayList<String>();
		
		List<FeatureElement> categories = f.getFeatureElementsByName("categories");

		for (FeatureElement categoryElement : categories) {
			System.out.println("CategoryElement:" + categoryElement.getValue());
			String[] cats = categoryElement.getValue().split(",");
			System.out.println("Cats=" + cats);
			for (int i=0; i<cats.length; i++) {
				returnList.add(cats[i].trim());
			}
		}
		return returnList;
	}
}
