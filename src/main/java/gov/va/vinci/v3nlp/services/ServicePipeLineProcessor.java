package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.StaticApplicationContext;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.util.Date;


public class ServicePipeLineProcessor {

    private String directoryToStoreResults;

    public void init() {
        if (!new File(getDirectoryToStoreResults()).exists()
                || !new File(getDirectoryToStoreResults()).isDirectory()) {
            throw new RuntimeException(getDirectoryToStoreResults()
                    + ": Is not a valid directory to store results.");
        }
    }

    @Async
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus) {
        Corpus returnCorpus = corpus;

        System.out.println("Begin pipeline processing [" + pipeLine.getPipeLineName() + "] at " + new Date());
        try {

            for (ServicePipeLineComponent comp : pipeLine.getServices()) {
                if (comp.getServiceUid() == null) {
                    continue;
                }
                System.out.println("\t\tComponent:" + comp.getServiceUid() + " Starting: " + new Date());
                NlpProcessingUnit bean = StaticApplicationContext.getApplicationContext().getBean(comp.getServiceUid(), NlpProcessingUnit.class);
                returnCorpus = bean.execute(comp.getConfiguration(), returnCorpus);
            }

            System.out.println("End pipeline processing [" + pipeLine.getPipeLineName() + "] at " + new Date());

            serializeObject(this.getDirectoryToStoreResults() + pipeLineId
                    + ".results",  new CorpusSummary(returnCorpus));
        } catch (Exception e) {
            e.printStackTrace();
            serializeObject(this.directoryToStoreResults + pipeLineId + ".err",
                    e);
        } finally {
            new File(directoryToStoreResults + pipeLineId + ".lck").delete();
        }

        return;
    }


    private void serializeObject(String path, Object e) {
        OutputStream os;
        try {
            os = new FileOutputStream(path);
            ObjectOutput oo = new ObjectOutputStream(os);
            oo.writeObject(e);
            oo.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public String getDirectoryToStoreResults() {
        return directoryToStoreResults;
    }

    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

}
