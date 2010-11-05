package gov.va.research.inlp;

import gov.va.vinci.cm.DocumentInterface;

import java.util.List;

public interface DocumentService {
	
	public List<DocumentInterface> getRandomDocuments(Integer numberToGet);

}
