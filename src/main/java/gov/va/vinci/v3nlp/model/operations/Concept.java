package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

public class Concept extends BaseOperation {
	private String expressionName;
	private String expression;
	private String code;
	private String captureGroups;

    public Concept() {
        this.setModuleName("Concept");
        this.getProvides().add("RegEx");
		this.getRequires().add("Fetch");
		this.getMustComeAfter().add("Output");
		this.setDescription("Multiple expresions can be entered by seperating them with a pipe '|' symbol. (ie- signed|discharged) These expressions are 'OR', not and.");
    }
	
	public String toConceptXml() {
		return 	  "   <concept>\n" + "      <def><![CDATA[(?i)" + getNullSafeString(this.expression) + "]]></def>\n"
				+ "      <capt_group_num>0</capt_group_num>\n"
				+ "      <type>RegEx</type>\n"
				+ "      <name>" + getNullSafeString(this.expressionName) + "</name>\n"
				+ "      <features>\n" + "         <feature>\n"
				+ "            <name>code</name>\n"
				+ "            <value><![CDATA[" + getNullSafeString(this.code)+ "]]></value>\n"
				+ "         </feature>\n" + "      </features>\n"
				+ "   </concept>\n";
	}
	
	private String getNullSafeString(String s) {
		if (s==null) {
			return "";
		} else {
			return s;
		}
	}

    public String getCaptureGroups() {
        return captureGroups;
    }

    public void setCaptureGroups(String captureGroups) {
        this.captureGroups = captureGroups;
    }

    public String getExpressionName() {
        return expressionName;
    }

    public void setExpressionName(String expressionName) {
        this.expressionName = expressionName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
