package gov.va.vinci.v3nlp.model;


import gov.va.vinci.cm.FormatInfo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public abstract class BaseNlpModule extends Object {
	private String moduleName = "";
	private List<String> provides  = new ArrayList<String>();
	private List<String> requires =  new ArrayList<String>();
	private boolean singleton = false;
	private List<String> mustComeAfter =  new ArrayList<String>();
	private List<String> mustComeBefore =  new ArrayList<String>();
	private FormatInfo formatInfo;
    private String description;
    private boolean keepFeatureInResult= true;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<String> getProvides() {
        return provides;
    }

    public void setProvides(List<String> provides) {
        this.provides = provides;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public List<String> getMustComeAfter() {
        return mustComeAfter;
    }

    public void setMustComeAfter(List<String> mustComeAfter) {
        this.mustComeAfter = mustComeAfter;
    }

    public List<String> getMustComeBefore() {
        return mustComeBefore;
    }

    public void setMustComeBefore(List<String> mustComeBefore) {
        this.mustComeBefore = mustComeBefore;
    }

    public FormatInfo getFormatInfo() {
        return formatInfo;
    }

    public void setFormatInfo(FormatInfo formatInfo) {
        this.formatInfo = formatInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isKeepFeatureInResult() {
        return keepFeatureInResult;
    }

    public void setKeepFeatureInResult(boolean keepFeatureInResult) {
        this.keepFeatureInResult = keepFeatureInResult;
    }
}