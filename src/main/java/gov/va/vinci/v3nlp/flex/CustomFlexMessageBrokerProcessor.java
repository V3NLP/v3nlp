package gov.va.vinci.v3nlp.flex;


import flex.messaging.MessageBroker;
import flex.messaging.io.PropertyProxyRegistry;
import org.apache.uima.cas.CAS;
import org.springframework.flex.config.MessageBrokerConfigProcessor;

public class CustomFlexMessageBrokerProcessor implements MessageBrokerConfigProcessor {

    @Override
    public MessageBroker processAfterStartup(MessageBroker broker) {
        PropertyProxyRegistry.getRegistry().
              register(CAS.class, new CASProxy());

              return broker;
    }

    @Override
    public MessageBroker processBeforeStartup(MessageBroker broker) {
        return broker;
    }
}
