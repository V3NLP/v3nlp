package gov.va.research.inlp.model.operations;

import gov.va.vinci.v3nlp.model.BaseNlpModule;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Sectionizer extends BaseNlpModule {

	private String[] sections;
	private boolean exclude=false;
}
