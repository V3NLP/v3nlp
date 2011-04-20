package gov.va.vinci.v3nlp.model;


public class ServicePipeLineComponent {
    private String serviceUid;
    private String configuration;

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

    public String toString() {
         return "ServiceUid: " + serviceUid + " -- Config: " + configuration;
    }
}
