package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.StaticApplicationContext;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Callable;

public class CallableDocumentServiceProcessor implements Callable {
    private static Log logger = LogFactory.getLog(CallableDocumentServiceProcessor.class);
    private DocumentInterface document;
    private ServicePipeLine pipeLine;


    public CallableDocumentServiceProcessor(ServicePipeLine p, DocumentInterface d) {
        this.document = d;
        this.pipeLine = p;
    }

    @Override
    public Object call() throws Exception {
        for (ServicePipeLineComponent comp : pipeLine.getServices()) {
            if (comp.getServiceUid() == null) {
                continue;
            }
            logger.info("\t\t[ " + pipeLine.getPipeLineName() + " ~~ " + document.getDocumentName() + " ] Component:" + comp.getServiceUid() + " Keep in final result:" + comp.isKeepAnnotationsInFinalResult());
            if (!ServiceListThreadLocal.get().containsKey(comp.getServiceUid())) {
                ServiceListThreadLocal.get().put(comp.getServiceUid(), StaticApplicationContext.getApplicationContext().getBean(comp.getServiceUid(), NlpProcessingUnit.class));
            }

            NlpProcessingUnit bean =ServiceListThreadLocal.get().get(comp.getServiceUid());
            document = bean.process(comp.getConfiguration(), document);
        }

        return document;
    }

    public DocumentInterface getDocument() {
        return this.document;
    }



}
