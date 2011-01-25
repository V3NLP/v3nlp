package gov.va.vinci.v3nlp.model.datasources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataServiceSource extends BaseNlpModule {
	public Integer numberOfDocuments;
	public String dataServiceUsername;
	public String dataServicePassword;
	public String dataServiceName;
}
