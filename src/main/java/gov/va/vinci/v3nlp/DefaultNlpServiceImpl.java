package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.service.SerializationService;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import gov.va.vinci.v3nlp.services.DatabaseRepositoryService;
import gov.va.vinci.v3nlp.services.ServicePipeLineProcessor;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
            CorpusSummary c = (CorpusSummary) this.deSerialize(pathOfResults
                    + pipeLineId + ".results");
            new File(pathOfResults + pipeLineId + ".results")
                    .delete();
            return c;
        } else if (new File(pathOfResults + pipeLineId + ".err")
                .exists()) {
            Exception e = (Exception) this
                    .deSerialize(pathOfResults + pipeLineId
                            + ".err");
           // new File(pathOfResults + pipeLineId + ".err").delete();
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
    public List<BatchJobStatus> jobsForUserToken(String userToken) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(userToken);

        File userPath = new File(pathOfResults);
        // User directory doesn't exit, no results.
        if (!userPath.exists()) {
           return new ArrayList<BatchJobStatus>();
        }

        if (!userPath.isDirectory()) {
            throw new RuntimeException("User path is a file, not a directory.");
        }

        List<BatchJobStatus> results = new ArrayList<BatchJobStatus>();

        // Iterate through files in the directory to get status.
        for (String file: userPath.list()) {
            Long lastModified = new File(pathOfResults + "/" + file).lastModified();
            if (file.endsWith(".lck")) {
                BatchJobStatus status = new BatchJobStatus();
                status.setRunDate(new Date(lastModified));
                status.setStatus("RUNNING");
                status.setPipeLineId(file.substring(0, file.lastIndexOf(".")));
                results.add(status);
            } else if (file.endsWith(".results")) {
                BatchJobStatus status = new BatchJobStatus();
                status.setRunDate(new Date(lastModified));
                status.setStatus("COMPLETE");
                status.setPipeLineId(file.substring(0, file.lastIndexOf(".")));
                results.add(status);
            }
        }

        return results;
    }

    /**
     * Given a pipeline id and user token, load the result and return it as a serialized string.
     * @param pipeLineId
     * @param userToken
     * @return the corpus in serialized form.
     */
    public String getSerializedResults(String pipeLineId, String userToken){
        CorpusSummary summary = this.getPipeLineResults(pipeLineId, userToken);
        return this.serializeCorpus(summary.getCorpus());
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