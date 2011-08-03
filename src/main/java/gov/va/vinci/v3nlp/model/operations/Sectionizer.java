package gov.va.vinci.v3nlp.model.operations;


public class Sectionizer extends BaseOperation {

	private String[] sections;
	private boolean exclude=false;
    private String customConfiguration;

    public Sectionizer()
    {
        this.setModuleName("Sectionizer");
		this.getProvides().add("section");
		this.getProvides().add("section_header");
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

    public String getCustomConfiguration() {
        return customConfiguration;
    }

    public void setCustomConfiguration(String customConfiguration) {
        this.customConfiguration = customConfiguration;
    }
}
