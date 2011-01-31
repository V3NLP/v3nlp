package gov.va.vinci.v3nlp.model.operations;


public class Tokenizer extends BaseOperation {

    public Tokenizer()
    {
        this.setModuleName("Tokenizer");
		this.getProvides().add("Token");
		this.getProvides().add("SpaceToken");
		this.getRequires().add("Fetch");
		this.getMustComeBefore().add("Output");
		this.getMustComeBefore().add("Concept");
		this.getMustComeBefore().add("MetamapConcept");
		this.getMustComeBefore().add("Negation");
		this.getMustComeBefore().add("Sectionizer");
		this.setSingleton(true);
    }
}
