package gov.va.vinci.v3nlp.services.other;

import gov.va.research.v3nlp.common.metamap.MetaMapServiceHttpInvoker;
import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.services.BaseNlpProcessingUnit;
import gov.va.vinci.v3nlp.services.ServicePipeLineProcessorImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Calls the Metamap Provider via HTTP Invoker.
 * Notes:
 * <ul>
 *     <li>Limited to mapping the previous modules pedigree if available.</li>
 * </ul>
 */
public class MetamapProviderServiceImpl extends BaseNlpProcessingUnit {

    private RestTemplate restTemplate;

    private MetaMapServiceHttpInvoker metamapService;

    private static Log logger = LogFactory.getLog(ServicePipeLineProcessorImpl.class);

    /**
     * Processing method.
     *
     * @param config The configuration is in XML, and in the format:
     *               <?xml version="1.0" encoding="ISO-8859-1" ?>
     *               <metamap-concepts>
     *               <semantic-group>ANAT</semantic-group>
     *               </metamap-concepts>
     *               This module only sends the previous modules annotations content to metamap.
     * @param d
     * @param previousModuleProvided The list of annotations the previous module created. This modules only sends those
     *      to metamap.
     * @return  a document with metamap annotations.
     */
    @Override
    public DocumentInterface process(String config, DocumentInterface d, List<NlpComponent> previousModuleProvided) {
        List<Annotation> toProcess = this.getProcessList(d, previousModuleProvided);
        List<String> semanticGroups = getSemanticGroups(config);
        Date dt= new Date();

        /**
         * Process annotations that need processed.
         *
         **/
        for (Annotation a : toProcess) {
            Document newDocument = null;
            try {
                Date mappingStart = new Date();
                newDocument = metamapService.getMapping(a.getContent(),
                        false, new ArrayList<String>(), semanticGroups);
                logger.debug("\t\t--------> Metamap mapping took: " + (new Date().getTime() - mappingStart.getTime()) + "ms");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            /** Add results back to the document. **/
            for (AnnotationInterface newAnnotation : newDocument.getAnnotations().getAll()) {
                for (Feature f: ((Annotation)newAnnotation).getFeatures()) {
                    f.getMetaData().setPedigree("INTERFACE:metamap");
                }
                newAnnotation.setBeginOffset(newAnnotation.getBeginOffset() + a.getBeginOffset());
                newAnnotation.setEndOffset(newAnnotation.getEndOffset() + a.getBeginOffset());


                // See if we already have an annotation at this spot. If so, just add the
                // new features to it, otherwise add new annotation;
                boolean foundExisting = false;
                for (AnnotationInterface ea: d.getAnnotations().getAll()) {
                    Annotation existingAnnotation = (Annotation)ea;
                    if (existingAnnotation.getBeginOffset() == newAnnotation.getBeginOffset() &&
                        existingAnnotation.getEndOffset() == newAnnotation.getEndOffset()) {
                        foundExisting = true;
                        for (Feature newFeature: ((Annotation) newAnnotation).getFeatures()) {
                            existingAnnotation.getFeatures().add(newFeature);
                        }

                    }
                }
                if (!foundExisting) {
                    d.addAnnotation(newAnnotation);
                }
            }
        }

        logger.debug("\t\t--------> Metamap processed document in: " + (new Date().getTime() - dt.getTime()) + "ms");

        return d;
    }

    private List<String> getSemanticGroups(String config) {
        List<String> results = new ArrayList<String>();
        for (String line : config.split(
                "\r\n|\r|\n")) {
            if (!org.apache.commons.validator.GenericValidator
                    .isBlankOrNull(line)) {
                if (line.contains("<semantic-group>")) {
                    results.add(line.substring(line.indexOf("<semantic-group>") + 16, line.lastIndexOf("</semantic-group>")));
                }
            }
        }
        return results;
    }

    private List<String> getCategoriesFromFeatureElements(Feature f) {
        List<String> returnList = new ArrayList<String>();

        Set<FeatureElement> categories = f
                .getFeatureElementsByName("categories");

        for (FeatureElement categoryElement : categories) {
            String[] cats = ((String) categoryElement.getValue()).split(",");
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
}
