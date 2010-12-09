package gov.va.research.inlp.model.datasources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.research.inlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataServiceSource extends BaseNlpModule {
	public Integer numberOfDocuments;
	public String dataServiceUsername;
	public String dataServicePassword;
	public String dataServiceName;
}
