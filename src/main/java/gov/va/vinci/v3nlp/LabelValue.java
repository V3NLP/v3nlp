package gov.va.vinci.v3nlp;

import lombok.Data;


public class LabelValue {
	private String label;
	private String value;
	
	
	public LabelValue(String label, String value) {
		this.label = label;
		this.value = value;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
