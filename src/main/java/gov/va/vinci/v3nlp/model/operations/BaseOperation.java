package gov.va.vinci.v3nlp.model.operations;


import gov.va.vinci.v3nlp.model.BaseNlpModule;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseOperation extends BaseNlpModule {

    private boolean keepFeatureInResult= true;

    public boolean isKeepFeatureInResult() {
        return keepFeatureInResult;
    }

    public void setKeepFeatureInResult(boolean keepFeatureInResult) {
        this.keepFeatureInResult = keepFeatureInResult;
    }
}