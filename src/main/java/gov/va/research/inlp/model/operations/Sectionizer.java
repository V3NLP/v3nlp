package gov.va.research.inlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.research.inlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class Sectionizer extends BaseNlpModule {

	private String[] sections;
	private boolean exclude=false;
}
