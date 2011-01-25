package gov.va.research.inlp.model.operations;

import gov.va.vinci.v3nlp.model.BaseNlpModule;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Negation extends BaseNlpModule {
	private String configuration;
	private Boolean useCustomConfiguration;
	
}
