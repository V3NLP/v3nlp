package gov.va.vinci.v3nlp.model.operations;

public class OParser extends BaseOperation {

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
