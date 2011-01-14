package gov.va.research.inlp.services;

import java.util.List;

public interface SectionizerService {

	public abstract String getDefaultSectionizerConfiguration()
			throws Exception;

	public abstract gov.va.vinci.cm.Corpus sectionize( String customSectionizerRules, gov.va.vinci.cm.Corpus _corpus);

	/**
	 * Get a list of available sections headers for the configured sectionizer.
	 * 
	 * @return A list of string section headers.
	 */
	public abstract List<String> getAvailableSectionHeaders();

}