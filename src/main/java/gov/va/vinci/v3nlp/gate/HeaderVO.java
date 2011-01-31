package gov.va.vinci.v3nlp.gate;


public class HeaderVO {
	private Integer captGroupNum;
	private String header;
	private String categories;

    public Integer getCaptGroupNum() {
        return captGroupNum;
    }

    public void setCaptGroupNum(Integer captGroupNum) {
        this.captGroupNum = captGroupNum;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
