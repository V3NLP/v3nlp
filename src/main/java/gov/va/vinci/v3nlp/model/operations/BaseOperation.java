/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model.operations;


import gov.va.vinci.v3nlp.model.BaseNlpModule;

public abstract class BaseOperation extends BaseNlpModule {

    private boolean keepFeatureInResult= true;

    public boolean isKeepFeatureInResult() {
        return keepFeatureInResult;
    }

    public void setKeepFeatureInResult(boolean keepFeatureInResult) {
        this.keepFeatureInResult = keepFeatureInResult;
    }
}