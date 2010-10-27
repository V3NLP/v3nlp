package gov.va.research.inlp;

import gov.va.research.inlp.model.PipeLine;

import java.util.List;

public interface NlpService {

	/**
	 * Given a pipeline, actually process it.
	 * @param dataToProcess
	 * @return
	 */
	public abstract gov.va.vinci.cm.Corpus processPipeLine(PipeLine dataToProcess);

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
	public String getDefaultSectionizerConfiguration() throws Exception;
	
	public abstract String getDefaultNegationConfiguration() throws Exception;

}