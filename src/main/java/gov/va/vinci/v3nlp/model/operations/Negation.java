package gov.va.vinci.v3nlp.model.operations;

public class Negation extends BaseOperation {
	private String configuration;
	private Boolean useCustomConfiguration;

    public Negation()
    {
        this.setModuleName("Negation");
		this.getProvides().add("Negation");
		this.getRequires().add("Fetch");
		this.getMustComeBefore().add("Output");
        this.setSingleton(true);
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Boolean getUseCustomConfiguration() {
        return useCustomConfiguration;
    }

    public void setUseCustomConfiguration(Boolean useCustomConfiguration) {
        this.useCustomConfiguration = useCustomConfiguration;
    }
}
