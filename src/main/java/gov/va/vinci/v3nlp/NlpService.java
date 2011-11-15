package gov.va.vinci.v3nlp;

import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.services.database.V3nlpDBRepository;

import java.sql.SQLException;
import java.util.List;

public interface NlpService {

    /**
     * Submit a pipeline for processing. This returns immediately with the id of the job. The job will then process
     * in the background.
     * @param pipeLine the pipeline definition to process.
     * @param corpus  the corpus to process.
     * @return the id of the pipeline.
     */
    public abstract String submitPipeLine(ServicePipeLine pipeLine, Corpus corpus);

    /**
     * Submit a pipeline for processing. This returns immediately with the id of the job. The job will then process
     * in the background.
     * @param pipeLine the pipeline definition to process.
     * @param corpus  the corpus to process.
     * @param dataServiceSourceList If pulling for a database, this is a list of the database information
     *      needed to query the database.
     * @return the id of the pipeline.
     * @throws SQLException
     */
    public String submitPipeLine(ServicePipeLine pipeLine, Corpus corpus, List<V3nlpDBRepository> dataServiceSourceList) throws SQLException;

    /**
     * Given a pipeLineId, determine if it is still processing, or if processing has completed.
     *
     * @param pipeLineId
     * @return the processing status of the pipeLine;
     */
    public abstract String getPipeLineStatus(String pipeLineId, String userToken);

    /**
     * For a pipeLineId, get the pipeLine results. Status should be checked first to
     * insure it has completed processing.
     *
     * @param pipeLineId
     * @return the results of the pipeline processing.
     */
    public abstract CorpusSummary getPipeLineResults(String pipeLineId, String userToken);

    /**
     * Serialize a corpus.
     *
     * @param c
     * @return The serialized corpus.
     */
    public abstract String serializeCorpus(Corpus c);

    /**
     * Deserialize a corpus
     *
     * @param content
     * @return the deserialized corpus.
     */
    public abstract Corpus deSerializeCorpus(String content);

    /**
     * Deserialize the corpus to a corpus summary.
     *
     * @param content
     * @return the corpus summary.
     */
    public abstract CorpusSummary deSerializeCorpusToCorpusSummary(String content);


    /**
     * Return a list of jobs and their status for a given user token.
     */
    public abstract List<BatchJobStatus> jobsForUserToken(String userToken);

    public abstract List<V3nlpDBRepository> getRepositories();

    public abstract String testDataService(V3nlpDBRepository ds, String loggedInUser);
}