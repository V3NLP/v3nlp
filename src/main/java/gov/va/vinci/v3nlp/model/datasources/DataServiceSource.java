package gov.va.vinci.v3nlp.model.datasources;

import gov.va.vinci.v3nlp.model.BaseNlpModule;


public class DataServiceSource extends BaseNlpModule {
    public Integer numberOfDocuments;
    public String dataServiceUsername;
    public String dataServicePassword;
    public String dataServiceName;

    public Integer getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(Integer numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public String getDataServiceUsername() {
        return dataServiceUsername;
    }

    public void setDataServiceUsername(String dataServiceUsername) {
        this.dataServiceUsername = dataServiceUsername;
    }

    public String getDataServicePassword() {
        return dataServicePassword;
    }

    public void setDataServicePassword(String dataServicePassword) {
        this.dataServicePassword = dataServicePassword;
    }

    public String getDataServiceName() {
        return dataServiceName;
    }

    public void setDataServiceName(String dataServiceName) {
        this.dataServiceName = dataServiceName;
    }
}
