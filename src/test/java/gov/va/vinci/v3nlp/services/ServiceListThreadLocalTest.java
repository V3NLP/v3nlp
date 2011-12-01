/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.fail;


public class ServiceListThreadLocalTest {

    @Test
    public void testSetGet() {
        HashMap<String, NlpProcessingUnit> serviceMap = new HashMap<String, NlpProcessingUnit>();
        
        serviceMap.put("ABC-123", null);

        ServiceListThreadLocal.set(serviceMap);
        
        assert(ServiceListThreadLocal.get().size() == 1);
        assert(ServiceListThreadLocal.get().containsKey("ABC-123"));
        ServiceListThreadLocal.unset();
        assert(ServiceListThreadLocal.get() != null);
        assert(ServiceListThreadLocal.get().size() == 0);
    }
    
    @Test
    public void testException() throws Exception {
        try {
            ServiceListThreadLocal.set(null);
            fail("Did not catch null exception on setter.");
        } catch (java.lang.IllegalArgumentException e) {

        }
    }
}
