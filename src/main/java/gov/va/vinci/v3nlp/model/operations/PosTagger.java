package gov.va.vinci.v3nlp.model.operations;




public class PosTagger extends BaseOperation {

    public PosTagger() {
        this.setModuleName("PosTagger");
		this.getProvides().add("PartOfSpeech");
		this.getRequires().add("Fetch");
		this.getRequires().add("Token");
		this.getRequires().add("SpaceToken");
		this.getRequires().add("SentenceSplitter");
		this.getMustComeBefore().add("Output");
		this.getMustComeBefore().add("MetamapConcept");
		this.setSingleton(true);
    }
}
