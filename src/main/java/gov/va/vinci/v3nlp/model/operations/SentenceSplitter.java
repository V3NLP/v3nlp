package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

public class SentenceSplitter extends BaseNlpModule {

    public SentenceSplitter()
    {
        	this.setModuleName("SentenceSplitter");
			this.getProvides().add("SentenceSplitter");
			this.getRequires().add("Fetch");
			this.getRequires().add("Token");
			this.getRequires().add("SpaceToken");
			this.getMustComeBefore().add("Output");
			this.getMustComeBefore().add("Concept");
			this.getMustComeBefore().add("MetamapConcept");
			this.getMustComeBefore().add("Sectionizer");
			this.setSingleton(true);
    }
}
