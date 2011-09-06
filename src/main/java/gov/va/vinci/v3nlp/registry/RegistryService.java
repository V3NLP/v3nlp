package gov.va.vinci.v3nlp.registry;


import gov.va.vinci.v3nlp.model.ServicePipeLine;

import java.util.List;

public interface RegistryService {

    /**
     * Get all Nlp Component Categories in the registry
     * @return  the list of component categories in the system.
     */
    public List<NlpComponentCategory> getNlpComponentCategoryList();

    /**
     * Returns a component for a given unique service id.
     * @param uid the unique service id to get
     * @return  the nlp component or null if the component was not found.
     */
    public NlpComponent getNlpComponent(String uid);


    public List<NlpAnnotation> getNlpAnnotationList();

    /**
     * Validates a pipeline. If there is a missing dependency, or validation issue, the
     * return value will have the failure mesage. An empty return string means a valid pipeline.
     * @param pipeLine the pipeline to validate.
     * @return An error message for the first validation failure encountered, or a empty
     * string if the pipeline is valid.
     */
    public String validatePipeline(ServicePipeLine pipeLine);

    public void init();

    public void refresh();

}
