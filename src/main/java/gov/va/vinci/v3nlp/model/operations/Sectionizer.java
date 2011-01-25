package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class Sectionizer extends BaseNlpModule {

	private String[] sections;
	private boolean exclude=false;
}