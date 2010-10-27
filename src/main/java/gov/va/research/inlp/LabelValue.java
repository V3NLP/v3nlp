package gov.va.research.inlp;

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
