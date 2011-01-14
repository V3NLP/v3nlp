package gov.va.research.inlp.services;

public interface ConceptFinderService {

	public gov.va.vinci.cm.Corpus conceptFinder(
			String regExFilter, String sectionFilter,
			gov.va.vinci.cm.Corpus _corpus);

}