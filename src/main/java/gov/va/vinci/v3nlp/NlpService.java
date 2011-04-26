package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;

public interface NlpService {


    public abstract String submitPipeLine(ServicePipeLine pipeLine, Corpus corpus);

    /**
     * Given a pipeLineId, determine if it is still processing, or if processing has completed.
     *
     * @param pipeLineId
     * @return the processing status of the pipeLine;
     */
    public abstract String getPipeLineStatus(String pipeLineId);

    /**
     * For a pipeLineId, get the pipeLine results. Status should be checked first to
     * insure it has completed processing.
     *
     * @param pipeLineId
     * @return the results of the pipeline processing.
     */
    public abstract CorpusSummary getPipeLineResults(String pipeLineId);

    public abstract String getPipeLineCasResult(String pipeLineId);

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

}