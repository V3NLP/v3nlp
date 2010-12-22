package gov.va.research.inlp;

import gov.va.research.inlp.model.PipeLine;
import gov.va.vinci.cm.Corpus;

import java.util.List;

public interface NlpService {

	/**
	 * Given a pipeline, submit for processing.
	 * @param dataToProcess
	 * @return
	 */
	public abstract String submitPipeLine(PipeLine dataToProcess, Corpus corpus);

	public abstract String getPipeLineStatus(String pipeLineId);
	
	public abstract Corpus getPipeLineResults(String pipeLineId);
	
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
	
	public abstract String getDefaultNegationConfiguration() throws Exception;
	
	public abstract String serializeCorpus(Corpus c);
	
	public abstract Corpus deSerializeCorpus(String content);

}