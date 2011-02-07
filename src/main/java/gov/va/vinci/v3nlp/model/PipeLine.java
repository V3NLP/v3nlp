package gov.va.vinci.v3nlp.model;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.model.operations.Concept;
import gov.va.vinci.v3nlp.model.operations.MetamapConcept;
import gov.va.vinci.v3nlp.model.operations.Negation;
import gov.va.vinci.v3nlp.model.operations.Sectionizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.validator.GenericValidator;


public class PipeLine {

	private List<BaseNlpModule> services = new ArrayList<BaseNlpModule>();
	private String pipeLineName = "";
    private String description;
	private String customSectionRules = null;
	private Corpus corpus;
	private Date createdDate;
	
	/**
	 * Get the negation module in this pipeline if there is one.
	 * 
	 * @return The negation module in the pipeline, if there is one, or null.
	 */
	public Negation getNegation() {
		for (BaseNlpModule p : services) {
			if (p instanceof Negation) {
				return (Negation) p;
			}
		}
		return null;
	}

	/**
	 * Return the name and content of each document in the corpus of this
	 * pipeline.
	 * 
	 * @return A hashtable with document name/content.
	 */
	public Hashtable<String, String> getCorpusContent() {
		Hashtable<String, String> result = new Hashtable<String, String>();
		for (DocumentInterface d : corpus.getDocuments()) {
			result.put(d.getDocumentName(), d.getContent());
		}
		return result;
	}

	/**
	 * Find the regular expression xml configuration for this pipeline.
	 * 
	 * @return the xml representation of this pipeline's regular expressions.
	 */
	public String getRegularExpressionConfiguration() {
		String regExXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
				+ "<concepts>\n";

		for (BaseNlpModule mod : services) {
			if (!(mod instanceof Concept)) {
				continue;
			}

			Concept re = (Concept) mod;

			if (GenericValidator.isBlankOrNull(re.getCode())) {
				re.setCode("Code");
			}
			if (GenericValidator.isBlankOrNull(re.getExpressionName())) {
				re.setExpressionName("Expression: " + re.getExpression());
			}
			regExXml += re.toConceptXml();
		}
		regExXml += "</concepts>";

		return regExXml;

	}

	/**
	 * Find the section modules in this pipeline, and create a configuration
	 * string for them.
	 * 
	 * @return The regular expression describing the section modules in this
	 *         pipeline.
	 */
	public String getSectionCriteriaExpression() {
		String result = "";
		String excludeSectionString = "";

		for (BaseNlpModule mod : services) {
			if (!(mod instanceof Sectionizer)) {
				continue;
			}
			Sectionizer s = (Sectionizer) mod;
			for (int i = 0; i < s.getSections().length; i++) {
				if (s.isExclude()) {
					excludeSectionString += "(category = " + s.getSections()[i]
							+ ") || ";
				} else {
					result += "(category = " + s.getSections()[i] + ") || ";
				}
			}

		}
		if (result.equals("") && excludeSectionString.equals("")) {
			return "ANY";
		} else {
			if (excludeSectionString.length() > 0) {
				excludeSectionString = "!("
						+ excludeSectionString.substring(0,
								excludeSectionString.length() - 3) + ")";
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 3);
			}
			if (!GenericValidator.isBlankOrNull(result) && !GenericValidator.isBlankOrNull(excludeSectionString)) {
				return result + " && " + excludeSectionString.trim();
			} else {
				return (result + " " + excludeSectionString).trim();
			}
		}
	}

	public List<String> getSectionList() {
		List<String> sections = new ArrayList<String>();
		for (BaseNlpModule mod : this.getServices()) {
			if (!(mod instanceof Sectionizer)) {
				continue;
			}
			Sectionizer s = (Sectionizer) mod;
			sections.addAll(Arrays.asList(s.getSections()));
		}
		return sections;
	}

	/**
	 * Determine if this pipeline has any section criteria.
	 * 
	 * @return true if it has section criteria, false if it does not.
	 */
	public Boolean hasSectionCriteria() {
		return hasOperation(Sectionizer.class);
	}

	/**
	 * Determine if this pipeline has any section criteria.
	 * 
	 * @return true if it has section criteria, false if it does not.
	 */
	public Boolean hasConcept() {
		return hasOperation(Concept.class);
	}
	
	public Boolean hasOperation(Class<? extends BaseNlpModule> c) {
		for (BaseNlpModule mod : services) {
			if (c.isInstance(mod)) {
				return true;
			}
		}
		return false;
		
	}

	public MetamapConcept getMetamapConcept() {
		for (BaseNlpModule mod : services) {
			if (mod instanceof MetamapConcept) {
				return (MetamapConcept) mod;
			}
		}
		return null;
	}

    public List<BaseNlpModule> getServices() {
        return services;
    }

    public void setServices(List<BaseNlpModule> services) {
        this.services = services;
    }

    public String getPipeLineName() {
        return pipeLineName;
    }

    public void setPipeLineName(String pipeLineName) {
        this.pipeLineName = pipeLineName;
    }

    public String getCustomSectionRules() {
        return customSectionRules;
    }

    public void setCustomSectionRules(String customSectionRules) {
        this.customSectionRules = customSectionRules;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


    
}