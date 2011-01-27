package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

public class Negation extends BaseNlpModule {
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
