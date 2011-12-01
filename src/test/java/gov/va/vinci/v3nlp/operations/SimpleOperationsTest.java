/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.operations;

import junit.framework.TestCase;
import gov.va.vinci.v3nlp.model.operations.Concept;
import gov.va.vinci.v3nlp.model.operations.Negation;
import gov.va.vinci.v3nlp.model.operations.Sectionizer;


public class SimpleOperationsTest extends TestCase {

	private final String TEST_NULL_CONCEPT_XML="   <concept>\n" 
		+ "      <def><![CDATA[(?i)]]></def>\n"
		
     				+ "      <capt_group_num>0</capt_group_num>\n"
 
     				+ "      <type>RegEx</type>\n"
     				+ "      <name></name>\n"
     				+ "      <features>\n" + "         <feature>\n"
     				+ "            <name>code</name>\n"
     				+ "            <value><![CDATA[]]></value>\n"
     				+ "         </feature>\n" + "      </features>\n"
     				+ "   </concept>\n";
	
	private final String TEST_CONCEPT_XML="   <concept>\n" 
		+ "      <def><![CDATA[(?i)My Exp]]></def>\n"
		
     				+ "      <capt_group_num>0</capt_group_num>\n"
 
     				+ "      <type>RegEx</type>\n"
     				+ "      <name>My Name</name>\n"
     				+ "      <features>\n" + "         <feature>\n"
     				+ "            <name>code</name>\n"
     				+ "            <value><![CDATA[code]]></value>\n"
     				+ "         </feature>\n" + "      </features>\n"
     				+ "   </concept>\n";
	
	public void testSimpleInstantiation() {
		Concept c = new Concept();
		junit.framework.Assert.assertNotNull("Null object.", c);
		
		Negation n = new Negation();
		junit.framework.Assert.assertNotNull("Null object.", n);
		
		Sectionizer  s = new Sectionizer();
		junit.framework.Assert.assertNotNull("Null object.", s);
		
	}
	
	public void testNullConcept() {
		Concept c = new Concept();

		junit.framework.Assert.assertEquals(TEST_NULL_CONCEPT_XML, c.toConceptXml());
	}
	
	public void testConcept() {
		Concept c = new Concept();
		c.setCode("code");
		c.setExpression("My Exp");
		c.setExpressionName("My Name");
		junit.framework.Assert.assertEquals(TEST_CONCEPT_XML, c.toConceptXml());
	}
}
