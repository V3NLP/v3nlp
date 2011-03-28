package gov.va.vinci.v3nlp.flex;

import flex.messaging.io.BeanProxy;

public class CASProxy extends BeanProxy {

    private static final long serialVersionUID = 1L;

    public Object getValue(Object instance, String propertyName) {
        System.out.println("In here!!!");

        Class propertyType = getBeanProperty(instance, propertyName).getType();
        Object result = super.getValue(instance, propertyName);

        return result;
    }

    public void setValue(Object object, String propName, Object value) {
        System.out.println("In setValue!");
        super.setValue(object, propName, value);
    }
}
