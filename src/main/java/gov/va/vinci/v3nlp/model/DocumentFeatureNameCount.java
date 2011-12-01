/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model;

import java.io.Serializable;

public class DocumentFeatureNameCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String documentId;
	private String featureName;
	private Long count;
	
	public DocumentFeatureNameCount() {
		
	}
	
	public DocumentFeatureNameCount(String documentId, String pedigree, Long count) {
		super();
		this.documentId = documentId;
		this.featureName = pedigree;
		this.count = count;
	}

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
