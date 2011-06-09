package gov.va.vinci.v3nlp.services;


import java.util.HashMap;

public class ServiceListThreadLocal {
    public static final ThreadLocal<HashMap<String, NlpProcessingUnit>> serviceMapThreadLocal = new ThreadLocal<HashMap<String, NlpProcessingUnit>>();

    public static void set(HashMap<String, NlpProcessingUnit> serviceMap) {
        serviceMapThreadLocal.set(serviceMap);
    }

    public static void unset() {
        serviceMapThreadLocal.remove();
    }

    public static HashMap<String, NlpProcessingUnit> get() {
        if (serviceMapThreadLocal.get() == null) {
            serviceMapThreadLocal.set(new HashMap<String, NlpProcessingUnit>());
        }
        return serviceMapThreadLocal.get();
    }
}
