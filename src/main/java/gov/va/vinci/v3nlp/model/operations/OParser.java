package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;


public class OParser extends BaseNlpModule {

    public OParser() {
        this.setModuleName("OParser");
        this.getProvides().add("OParser");
		this.getRequires().add("Fetch");
		this.getRequires().add("Token");
		this.getRequires().add("SpaceToken");
		this.getRequires().add("PosTagger");
		this.getMustComeBefore().add("Output");
		this.getMustComeBefore().add("MetamapConcept");
        this.setSingleton(true);
    }
}
