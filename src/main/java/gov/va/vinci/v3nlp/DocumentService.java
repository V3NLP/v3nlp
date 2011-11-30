/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.DocumentInterface;

import java.util.List;

public interface DocumentService {
	
	public List<DocumentInterface> getRandomDocuments(Integer numberToGet);

}
