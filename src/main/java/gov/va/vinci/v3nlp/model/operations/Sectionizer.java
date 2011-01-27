package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

public class Sectionizer extends BaseNlpModule {

	private String[] sections;
	private boolean exclude=false;

    public Sectionizer()
    {
        this.setModuleName("Sectionizer");
		this.getProvides().add("Sectionizer");
		this.getRequires().add("Fetch");
		this.getMustComeBefore().add("Concept");
		this.getMustComeBefore().add("Output");
    }

    public String[] getSections() {
        return sections;
    }

    public void setSections(String[] sections) {
        this.sections = sections;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }
}
