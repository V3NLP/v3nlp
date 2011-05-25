package gov.va.vinci.v3nlp.services;

import gov.va.research.v3nlp.common.metamap.MetaMapServiceHttpInvoker;
import gov.va.vinci.cm.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MetamapProviderServiceImpl implements NlpProcessingUnit {

    private RestTemplate restTemplate;

    private MetaMapServiceHttpInvoker metamapService;

    /**
     * Processing method.
     *
     * @param config The configuration is in XML, and in the format:
     *               <?xml version="1.0" encoding="ISO-8859-1" ?>
     *               <metamap-concepts>
     *               <semantic-group>ANAT</semantic-group>
     *               </metamap-concepts>
     *               <sections>
     *               <section>DIAG</section>
     *               <section>ADMIN</section>
     *               <section>ADM</section>
     *               </sections>
     *               <p/>
     *               Sections limits processing to pre-annotated sections via the HitexSectionizerImpl. Leave
     *               sections element off to process the entire document.
     * @param d
     * @return
     */
    @Override
    public DocumentInterface process(String config, DocumentInterface d) {
        /**
         *
         * need sections and metamap concept lists.
         */
        List<String> sections = getSectionList(config);
        List<String> semanticGroups = getSemanticGroups(config);

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
        return d;
    }

    private List<String> getSectionList(String config) {
        List<String> results = new ArrayList<String>();
        boolean inSections = false;
        boolean exclude = false;

        for (String line : config.split(
                "\r\n|\r|\n")) {
            if (!org.apache.commons.validator.GenericValidator
                    .isBlankOrNull(line)) {

                if (line.trim().equals("<sections>")) {
                    inSections = true;
                    exclude = false;
                }
                if (line.trim().equals("<sections exclude='true'>")) {
                    inSections = true;
                    exclude = true;
                }

                if (inSections && !exclude && line.contains("<section>")) {
                    String section = line.substring(line.indexOf("<section>") + 9, line.lastIndexOf("</section>"));
                    results.add(section);
                }
            }
        }
        return results;
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

    @Override
    public void initialize() {
        // No-op in this implementation.
    }

    @Override
    public void destroy() {
        // No-op in this implementation.
    }
}
