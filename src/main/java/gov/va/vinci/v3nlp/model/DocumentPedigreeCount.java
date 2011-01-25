package gov.va.vinci.v3nlp.model;

import java.io.Serializable;

import lombok.Data;

@Data
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
	
}
