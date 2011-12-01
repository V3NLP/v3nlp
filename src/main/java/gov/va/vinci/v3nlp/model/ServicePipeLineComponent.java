/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model;


public class ServicePipeLineComponent {
    private String serviceUid;
    private String configuration;
    private boolean keepAnnotationsInFinalResult;


    public String getServiceUid() {
        return serviceUid;
    }

    public void setServiceUid(String serviceUid) {
        this.serviceUid = serviceUid;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public boolean isKeepAnnotationsInFinalResult() {
        return keepAnnotationsInFinalResult;
    }

    public void setKeepAnnotationsInFinalResult(boolean keepAnnotationsInFinalResult) {
        this.keepAnnotationsInFinalResult = keepAnnotationsInFinalResult;
    }

    public String toString() {
         return "ServiceUid: " + serviceUid + " -- Config: " + configuration;
    }



}
