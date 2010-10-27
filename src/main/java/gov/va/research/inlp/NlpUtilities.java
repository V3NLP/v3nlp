package gov.va.research.inlp;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.Feature;

public class NlpUtilities {

	public static Annotation convertAnnotation(gate.Annotation gateAnnotation)
	{
		Annotation resultAnnotation = new Annotation(gateAnnotation.getId());		
		resultAnnotation.setEndOffset(gateAnnotation.getEndNode().getOffset().intValue());
		resultAnnotation.setBeginOffset(gateAnnotation.getStartNode().getOffset().intValue());

		// Copy Features
		for (Object o: gateAnnotation.getFeatures().keySet()) {
			Feature f = new Feature((String)o, (String)gateAnnotation.getFeatures().get(o));
		
			if (((String)o).equals("name")) {
				resultAnnotation.addFeature( (String)gateAnnotation.getFeatures().get("type"),  (String)gateAnnotation.getFeatures().get(o));
			}
			
			// TODO Set Original Content, and possibly theNumWhiteSpaceThatFollows if available. 
			resultAnnotation.addFeature(f);
		}
		
		return resultAnnotation;		
	}
}
