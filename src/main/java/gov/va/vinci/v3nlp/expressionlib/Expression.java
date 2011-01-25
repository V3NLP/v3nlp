package gov.va.vinci.v3nlp.expressionlib;

import java.util.Date;

import lombok.Data;

@Data
public class Expression {
	Integer id;
	String expression;
	String title;
	String description;
	String createdBy;
	String matches;
	String nonMatches;
	String citation;
	Date createdDate;
	
	
}
