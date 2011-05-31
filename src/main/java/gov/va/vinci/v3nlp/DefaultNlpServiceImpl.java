package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.service.SerializationService;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import gov.va.vinci.v3nlp.services.DatabaseRepositoryService;
import gov.va.vinci.v3nlp.services.ServicePipeLineProcessor;
import gov.va.vinci.v3nlp.services.ServicePipeLineProcessorImpl;

import java.io.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Default NlpService implementation that V3NLP Keywords.
 *
 */
public class DefaultNlpServiceImpl implements NlpService {

    private String directoryToStoreResults;

    private SerializationService serializationService;

    private ServicePipeLineProcessor servicePipeLineProcessor;

    private DatabaseRepositoryService databaseRepositoryService;

    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

    public void setSerializationService(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    public void init() {
        if (!new File(directoryToStoreResults).exists()
                || !new File(directoryToStoreResults).isDirectory()) {
            throw new RuntimeException(directoryToStoreResults
                    + ": Is not a valid directory to store results.");
        }
    }

    @Override
    public CorpusSummary getPipeLineResults(String pipeLineId, String userToken) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(userToken);

        if (new File(pathOfResults + pipeLineId + ".results")
                .exists()) {
            CorpusSummary c = (CorpusSummary) this.deSerialize(this.directoryToStoreResults
                    + pipeLineId + ".results");
            new File(pathOfResults + pipeLineId + ".results")
                    .delete();
            return c;
        } else if (new File(pathOfResults + pipeLineId + ".err")
                .exists()) {
            Exception e = (Exception) this
                    .deSerialize(pathOfResults + pipeLineId
                            + ".err");
            new File(pathOfResults + pipeLineId + ".err").delete();
            throw new RuntimeException(e);
        } else {
            throw new RuntimeException("Results not found.");
        }
    }

    @Override
    public String getPipeLineStatus(String pipeLineId, String userToken) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(userToken);
        if (new File(pathOfResults + pipeLineId + ".lck").exists()) {
            return "PROCESSING";
        }
        if (new File(pathOfResults + pipeLineId + ".err").exists()) {
            return "ERROR";
        }
        if (new File(pathOfResults + pipeLineId + ".results").exists()) {
            return "COMPLETE";
        }

        return null;
    }

    @Override
    public String submitPipeLine(ServicePipeLine pipeLine, Corpus corpus, List<DataServiceSource> dataServiceSourceList)
            throws SQLException {


        System.out.println("Got Database Services: " + dataServiceSourceList);

        for (DataServiceSource ds : dataServiceSourceList) {
            List<DocumentInterface> docs = this.databaseRepositoryService.getDocuments(ds);

            for (DocumentInterface di : docs) {
                System.out.println("Adding document: " + di);
                corpus.addDocument(di);
            }
        }
        return this.submitPipeLine(pipeLine, corpus);
    }

    @Override
    public String submitPipeLine(ServicePipeLine pipeLine, Corpus corpus) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(pipeLine.getUserToken());

         // Make sure output path exists, or create it.
        if (!new File(pathOfResults).exists()) {
           new File(pathOfResults).mkdir();
        }

        try {
            String pipeLineId = new Date().getTime() + "-"
                    + pipeLine.hashCode();
            new File(pathOfResults + pipeLineId + ".lck").createNewFile();

            servicePipeLineProcessor.processPipeLine(pipeLineId, pipeLine, corpus);
            return pipeLineId;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String jobsForUserToken(String userToken) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(userToken);

        File userPath = new File(pathOfResults);
        // User directory doesn't exit, no results.
        if (!userPath.exists()) {
           return null;
        }

        if (!userPath.isDirectory()) {
            throw new RuntimeException("User path is a file, not a directory.");
        }

        // Iterate through files in the directory to get status.
        // TODO Implement.
        for (String file: userPath.list()) {
            System.out.println("File=" + file);
        }

        return "";
    }

    public String serializeCorpus(Corpus c) {
        return serializationService.serialize(c);
    }

    public Corpus deSerializeCorpus(String content) {
        return serializationService.deserialize(content, Corpus.class);
    }

    public CorpusSummary deSerializeCorpusToCorpusSummary(String content) {
        return new CorpusSummary(deSerializeCorpus(content));
    }

    /************************************************************************************************************
     * Setters Methods Below.
     ***********************************************************************************************************/
    public void setServicePipeLineProcessor(ServicePipeLineProcessor servicePipeLineProcessor) {
        this.servicePipeLineProcessor = servicePipeLineProcessor;
    }


    public void setDatabaseRepositoryService(DatabaseRepositoryService databaseRepositoryService) {
        this.databaseRepositoryService = databaseRepositoryService;
    }

    /************************************************************************************************************
     * Private Methods Below.
     ***********************************************************************************************************/

    private Object deSerialize(String path) {
        try {
            InputStream is = new FileInputStream(new File(path));
            ObjectInput oi = new ObjectInputStream(is);
            Object newObj = oi.readObject();
            oi.close();
            return newObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}