/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;


import java.util.HashMap;

/**
 * Thread local containing NLP Processing unit keys/objects.
 *
 * This allows documents on the same thread to re-use processing units, instead of re-initializing
 * one.
 */
public class ServiceListThreadLocal {
    public static final ThreadLocal<HashMap<String, NlpProcessingUnit>> serviceMapThreadLocal = new ThreadLocal<HashMap<String, NlpProcessingUnit>>();

    /**
     * Set the map of Service UID's / Processing units.
     * @param serviceMap the service maps. String is the service UID, NLPProcessingUnit is the processing unit.
     *                   <strong>Note: serviceMap cannot be null.</strong>
     *
     */
    public static void set(HashMap<String, NlpProcessingUnit> serviceMap) {
        if (serviceMap == null) {
             throw new IllegalArgumentException("Service map cannot be null.");
        }
        serviceMapThreadLocal.set(serviceMap);
    }

    /**
     * Remove the thread local.
     */
    public static void unset() {
        serviceMapThreadLocal.remove();
    }

    /**
     * Retrieve the service map.
     * @return the service map. Key is the service uid, object is the NlpProcessingUnit. If no
     * service map has been set, this returns and empty HashMap.
     *
     */
    public static HashMap<String, NlpProcessingUnit> get() {
        if (serviceMapThreadLocal.get() == null) {
            serviceMapThreadLocal.set(new HashMap<String, NlpProcessingUnit>());
        }
        return serviceMapThreadLocal.get();
    }
}
