package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.PipeLine;
import org.apache.uima.jcas.JCas;

import java.util.List;

public interface NlpService {

	/**
	 * Given a pipeline, submit for processing.
	 * @param dataToProcess
	 * @return
	 */
	public abstract String submitPipeLine(PipeLine dataToProcess, Corpus corpus);

	/**
	 * Given a pipeLineId, determine if it is still processing, or if processing has completed. 
	 * @param pipeLineId
	 * @return the processing status of the pipeLine;
	 */
	public abstract String getPipeLineStatus(String pipeLineId);
	
	/**
	 * For a pipeLineId, get the pipeLine results. Status should be checked first to
	 * insure it has completed processing. 
	 * @param pipeLineId
	 * @return the results of the pipeline processing. 
	 */
	public abstract CorpusSummary getPipeLineResults(String pipeLineId);

    public abstract String getPipeLineCasResult(String pipeLineId);

	/**
	 * Returns the list of section headers available in this service.
	 * 
	 * @return the list of section headers available in this service.
	 */
	public abstract List<String> getAvailableSectionHeaders();
	
	/**
	 * Gets the raw sectionizer configuration. 
	 * @return the raw sectionizer configuration file. 
	 * @throws Exception
	 */
	public abstract String getDefaultSectionizerConfiguration() throws Exception;
	
	/**
	 * Returns the default negation configuration available in this service. 
	 * @return the default negation configuration
	 * @throws Exception
	 */
	public abstract String getDefaultNegationConfiguration() throws Exception;
	
	/**
	 * Serialize a corpus. 
	 * @param c
	 * @return The serialized corpus. 
	 */
	public abstract String serializeCorpus(Corpus c);
	
	/**
	 * Deserialize a corpus
	 * @param content
	 * @return the deserialized corpus. 
	 */
	public abstract Corpus deSerializeCorpus(String content);

	/**
	 * Deserialize the corpus to a corpus summary. 
	 * @param content
	 * @return the corpus summary. 
	 */
	public abstract CorpusSummary deSerializeCorpusToCorpusSummary(String content);

}