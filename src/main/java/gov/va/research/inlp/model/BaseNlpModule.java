package gov.va.research.inlp.model;


import gov.va.vinci.cm.FormatInfo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public abstract class BaseNlpModule extends Object {
	private String moduleName = "";
	private List<String> provides  = new ArrayList<String>();
	private List<String> requires =  new ArrayList<String>();
	private boolean singleton = false;
	private List<String> mustComeAfter =  new ArrayList<String>();
	private List<String> mustComeBefore =  new ArrayList<String>();
	private FormatInfo formatInfo;
}
