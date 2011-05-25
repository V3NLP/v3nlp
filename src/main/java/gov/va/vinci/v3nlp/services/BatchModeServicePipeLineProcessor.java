package gov.va.vinci.v3nlp.services;


import gate.CorpusController;
import gate.creole.ResourceInstantiationException;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.registry.RegistryService;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class BatchModeServicePipeLineProcessor implements ServicePipeLineProcessor {
    private static final Logger LOG = Logger.getLogger(BatchModeServicePipeLineProcessor.class.getName());

    @Override
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus) {
      /**
        int numthreads = 3;
        LOG.info("Executing Application with " + numthreads + " threads");

        for (int i = 0; i < numthreads; i++) {
            CorpusController corpusController = null;
            if (i == (numthreads - 1)) {
                corpusController = this.templateCorpusController;
            } else {
                corpusController = (CorpusController) gate.Factory.duplicate(this.templateCorpusController);
            }
            ReportIteratingController reportController = new ReportIteratingController(corpusController);
            pool.execute(reportController);
        }
        pool.shutdown();
        while (!pool.isTerminated())
            Thread.yield();
        LOG.info("Finished Application");
                                        **/
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDirectoryToStoreResults() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setRegistryService(RegistryService registryService) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
    public class CorpusIteratingController implements Runnable {
        private Corpus controller;

        public CorpusIteratingController(Corpus c) throws PersistenceException, ResourceInstantiationException, IOException {
            this.controller = c;
        }

        public void run() {
            /**

            try {              /**
                Report report = null;
                while ((report = nextReport()) != null) {
                    if (report.getText() == null) {
                        LOG.warning("Report " + report.getId().getId()
                                + " has no content");
                    } else {
                        gate.Document doc = gate.Factory.newDocument(report
                                .getText());
                        doc.setName(report.getId().getId());
                        System.out.println(report.getId().getId());
                        Corpus corpus = controller.getCorpus();
                        corpus.add(doc);
                        controller.execute();
//						String docString = doc.toXml();
//						File out = new File(outDir, doc.getName() + ".gate.xml");
//						FileWriter fw = new FileWriter(out);
//						fw.write(docString);
//						fw.close();
                        corpus.clear();
                        gate.Factory.deleteResource(doc);
                    }
                }
            } catch (GateException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private Corpus nextReport() {
        synchronized (this.reportIterator) {
            if (this.reportIterator.hasNext())
                return this.reportIterator.next();
            else
                return null;
        }
    } **/
}
