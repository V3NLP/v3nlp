package gov.va.vinci.v3nlp.model.operations;

public class SentenceSplitter extends BaseOperation {

    public SentenceSplitter()
    {
        	this.setModuleName("SentenceSplitter");
			this.getProvides().add("Sentence");
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
