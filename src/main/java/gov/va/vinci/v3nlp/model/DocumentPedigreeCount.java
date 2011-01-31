package gov.va.vinci.v3nlp.model;

import java.io.Serializable;

public class DocumentPedigreeCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String documentId;
	private String pedigree;
	private Long count;
	
	public DocumentPedigreeCount() {
		
	}
	
	public DocumentPedigreeCount(String documentId, String pedigree, Long count) {
		super();
		this.documentId = documentId;
		this.pedigree = pedigree;
		this.count = count;
	}

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
