/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.gate;

import gate.AnnotationSet;
import gate.Document;
import gate.ProcessingResource;
import gate.util.InvalidOffsetException;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.v3nlp.NlpUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NOTE: ONLY PROPOGATES NP's forward.
 */
public class OParserService extends GenericGateService {


    public OParserService(ProcessingResource resource) {
        super(resource);
    }

    /**
         * This returns a list of common model annotations from a gate document.
         *
         * @param gateDoc the gate document to get annotations from.
         * @param offset  Because we only send pieces of a larger document, this offset is the spot in the larger document
         *                where this piece resides. It is added to the gate annotation to get offsets relative to the larger document.
         * @return
         * @throws gate.util.InvalidOffsetException
         */
        protected List<Annotation> processDocumentForReturn(Document gateDoc, Integer offset, String pedigree) throws InvalidOffsetException {
            AnnotationSet annotations = gateDoc.getAnnotations();
            List<Annotation> results = new ArrayList<Annotation>();
            Iterator<gate.Annotation> i = annotations.iterator();
            while (i.hasNext()) {
                gate.Annotation a = i.next();
                // This feature is set on existing annotations converted from common model.
                if (!a.getFeatures().containsKey("V3NLP-CONVERTED-FROM-COMMON-MODEL")) {
                    if (a.getFeatures().get("type").equals("NP")) {
                        results.add(NlpUtilities.convertAnnotation(a,
                            gateDoc.getContent().getContent(a.getStartNode().getOffset(), a.getEndNode().getOffset()).toString(),
                            offset, pedigree));
                    }
                }
            }
            return results;
        }

    @Override
    public void initialize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
