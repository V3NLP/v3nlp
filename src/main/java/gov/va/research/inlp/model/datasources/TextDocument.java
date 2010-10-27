package gov.va.research.inlp.model.datasources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.research.inlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class TextDocument extends BaseNlpModule {
	private String dataText;
	private String sourceName;
}
