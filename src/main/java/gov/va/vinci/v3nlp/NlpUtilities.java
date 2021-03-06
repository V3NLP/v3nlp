/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;
import org.apache.commons.validator.GenericValidator;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NLP Related utilities used within the system.
 */
public class NlpUtilities {

    /**
     * Converts a gate annotation to a common model annotation.
     * @param gateAnnotation
     * @param content
     * @param offset
     * @param pedigree
     * @return
     */
    public static Annotation convertAnnotation(gate.Annotation gateAnnotation,
			String content, Integer offset, String pedigree) {
        Annotation resultAnnotation = new Annotation(gateAnnotation.getId());

            resultAnnotation.setBeginOffset(gateAnnotation.getStartNode()
                    .getOffset().intValue() + offset);
            resultAnnotation.setEndOffset(gateAnnotation.getEndNode().getOffset()
                    .intValue() + offset);
            Feature feature = new Feature((String) gateAnnotation.getFeatures()
                    .get("type"), (String) gateAnnotation.getFeatures().get("name"));
            feature.getFeatureElements().add(new FeatureElement("type", gateAnnotation.getType()));
            feature.getMetaData().setCreatedDate(new Date());
            feature.getMetaData().setPedigree(pedigree);


            if ("concept".equals(gateAnnotation.getType())) {
                 feature.setFeatureName(gateAnnotation.getFeatures().get("name").toString().trim());
            } else if (gateAnnotation.getType() != null && !GenericValidator.isBlankOrNull(gateAnnotation.getType().toString())) {
                feature.setFeatureName(gateAnnotation.getType().toString().trim());
            } else {
                feature.setFeatureName(feature.getMetaData().getPedigree());
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
                } else if (j == null) {

                } else {
                    throw new RuntimeException("Unknown Feature type.");
                }
            }

            resultAnnotation.setOriginalContent(content);
            resultAnnotation.getFeatures().add(feature);
            return resultAnnotation;

    }

    /**
     * Converts a gate annotations to a common model annotation.
     * @param gateAnnotation
     * @param content
     * @param pedigree
     * @return
     */
	@SuppressWarnings("unchecked")
	public static Annotation convertAnnotation(gate.Annotation gateAnnotation,
			String content, String pedigree) {
        return(convertAnnotation(gateAnnotation, content, 0, pedigree));
	}

    /**
     * Validate if a string contains only characters and numbers.
     * @param s  String to test.
     * @return  true if the string is only characters and numbers, false if any other characters
     *  are present.
     */
    public static boolean isValidNameNumbersAndCharacters(String s) {
        String regExp="^[a-zA-Z_0-9]+$";

        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        return(m.matches());
    }
}
