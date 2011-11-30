/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp;




public class LabelValue {
	private String label;
	private String value;
	
	
	public LabelValue(String label, String value) {
		this.label = label;
		this.setValue(value);
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
