package gov.va.vinci.v3nlp.model.datasources;


import gov.va.vinci.v3nlp.model.BaseNlpModule;

public class TextDocument extends BaseNlpModule {
	private String dataText;
	private String sourceName;

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
