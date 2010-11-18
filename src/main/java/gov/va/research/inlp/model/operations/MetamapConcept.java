package gov.va.research.inlp.model.operations;

import gov.va.research.inlp.model.BaseNlpModule;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MetamapConcept extends BaseNlpModule {

	private String[] semanticGroups;

}
