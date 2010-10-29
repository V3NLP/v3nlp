package gov.va.research.inlp;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;

public class NlpUtilities {

	public static Annotation convertAnnotation(gate.Annotation gateAnnotation, String content)
	{
		Annotation resultAnnotation = new Annotation(gateAnnotation.getId());		
		resultAnnotation.setEndOffset(gateAnnotation.getEndNode().getOffset().intValue());
		resultAnnotation.setBeginOffset(gateAnnotation.getStartNode().getOffset().intValue());

		Feature feature = new Feature( (String)gateAnnotation.getFeatures().get("type"),  (String)gateAnnotation.getFeatures().get("name"));

		// Copy Features
		for (Object o: gateAnnotation.getFeatures().keySet()) {
			feature.getFeatureElements().add(new FeatureElement((String)o, (String)gateAnnotation.getFeatures().get(o)));
		}

		// TODO set theNumWhiteSpaceThatFollows if available. 
		resultAnnotation.setOriginalContent(content);
		resultAnnotation.getFeatures().add(feature);
		return resultAnnotation;		
	}
}
