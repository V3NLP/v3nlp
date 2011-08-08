package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.StaticApplicationContext;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CallableDocumentServiceProcessor implements Callable {
    private static Log logger = LogFactory.getLog(CallableDocumentServiceProcessor.class);
    private DocumentInterface document;
    private ServicePipeLine pipeLine;
    private Map serviceMap;

    public CallableDocumentServiceProcessor(ServicePipeLine p, DocumentInterface d, Map serviceMap) {
        this.document = d;
        this.pipeLine = p;
        this.serviceMap = serviceMap;
    }

    @Override
    public Object call() throws Exception {
        List<NlpComponentProvides> previousModuleProvided = null;

        for (ServicePipeLineComponent comp : pipeLine.getServices()) {
            if (comp.getServiceUid() == null) {
                continue;
            }
            logger.info("\t\t[ " + pipeLine.getPipeLineName() + " ~~ " + document.getDocumentName() + " ] Component:" + comp.getServiceUid() + " Keep in final result:" + comp.isKeepAnnotationsInFinalResult());
            if (!ServiceListThreadLocal.get().containsKey(comp.getServiceUid())) {
                ServiceListThreadLocal.get().put(comp.getServiceUid(), StaticApplicationContext.getApplicationContext().getBean(comp.getServiceUid(), NlpProcessingUnit.class));
            }

            NlpProcessingUnit bean =ServiceListThreadLocal.get().get(comp.getServiceUid());
            document = bean.process(comp.getConfiguration(), document, previousModuleProvided);


            if (!"hitex.gate.Tokenizer".equals(comp.getServiceUid())) {
                previousModuleProvided = ((NlpComponent)serviceMap.get(comp.getServiceUid())).getProvides();
            }
        }
        return document;
    }

    public DocumentInterface getDocument() {
        return this.document;
    }



}
