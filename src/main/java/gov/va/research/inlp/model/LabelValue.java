package gov.va.research.inlp.model;

import lombok.Data;

@Data
public class LabelValue {

	private String label;
	private String value;
	
	public LabelValue(String label, String value) {
		this.label = label;
		this.value = value;
	}
}
