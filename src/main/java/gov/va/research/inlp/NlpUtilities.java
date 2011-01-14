package gov.va.research.inlp;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class NlpUtilities {

	public static Annotation convertAnnotation(gate.Annotation gateAnnotation,
			String content) {
		Annotation resultAnnotation = new Annotation(gateAnnotation.getId());
		
		resultAnnotation.setBeginOffset(gateAnnotation.getStartNode()
				.getOffset().intValue());
		resultAnnotation.setEndOffset(gateAnnotation.getEndNode().getOffset()
				.intValue());
		Feature feature = new Feature((String) gateAnnotation.getFeatures()
				.get("type"), (String) gateAnnotation.getFeatures().get("name"));
		feature.getFeatureElements().add(new FeatureElement("type", gateAnnotation.getType()));
		feature.getMetaData().setCreatedDate(new Date());
		if (gateAnnotation.getFeatures().get("type") != null) {
			feature.getMetaData().setPedigree((String) gateAnnotation.getFeatures().get("type"));
		} else {
			feature.getMetaData().setPedigree(gateAnnotation.getType());	
		}
		

		// Copy Features
		for (Object o : gateAnnotation.getFeatures().keySet()) {
			Object j = gateAnnotation.getFeatures().get(o);
			if (j instanceof String) {
				feature.getFeatureElements().add(
						new FeatureElement((String) o, (String) j));
			} else if (j instanceof List) {
				// TODO Implement
			} else if (j instanceof Set) {
				// TODO Implement
			} else {
				System.out.println(j);
				throw new RuntimeException("Unknown Feature type.");
			}
		}
		
		// TODO set theNumWhiteSpaceThatFollows if available.
		resultAnnotation.setOriginalContent(content);
		resultAnnotation.getFeatures().add(feature);
		return resultAnnotation;
	}
}
