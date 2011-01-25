package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class Negation extends BaseNlpModule {
	private String configuration;
	private Boolean useCustomConfiguration;
	
}
