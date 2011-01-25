package gov.va.vinci.v3nlp.model.datasources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class TextDocument extends BaseNlpModule {
	private String dataText;
	private String sourceName;
}
