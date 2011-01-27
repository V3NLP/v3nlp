package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;


public class PosTagger extends BaseNlpModule {

    public PosTagger() {
        this.setModuleName("PosTagger");
		this.getProvides().add("PosTagger");
		this.getRequires().add("Fetch");
		this.getRequires().add("Token");
		this.getRequires().add("SpaceToken");
		this.getRequires().add("SentenceSplitter");
		this.getMustComeBefore().add("Output");
		this.getMustComeBefore().add("MetamapConcept");
		this.setSingleton(true);
    }
}
