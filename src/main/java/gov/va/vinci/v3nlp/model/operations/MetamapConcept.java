package gov.va.vinci.v3nlp.model.operations;

import gov.va.vinci.v3nlp.model.BaseNlpModule;

import java.util.Arrays;


public class MetamapConcept extends BaseOperation {

	private String[] semanticGroups;

    public MetamapConcept()
    {
        this.setModuleName("MetamapConcept");
		this.getProvides().add("UMLSConcept");
		this.getRequires().add("Fetch");
		this.getMustComeBefore().add("Output");
		this.setDescription("Metamap concept service.");
    }

    public String[] getSemanticGroups() {
        return semanticGroups;
    }

    public void setSemanticGroups(String[] semanticGroups) {
        this.semanticGroups = semanticGroups;
    }
}
