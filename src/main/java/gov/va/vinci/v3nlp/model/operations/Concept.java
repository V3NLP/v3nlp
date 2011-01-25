package gov.va.vinci.v3nlp.model.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import gov.va.vinci.v3nlp.model.BaseNlpModule;

@Data
@EqualsAndHashCode(callSuper=false)
public class Concept extends BaseNlpModule {
	private String expressionName;
	private String expression;
	private String code;
	private String captureGroups;
	
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
}
