package gov.va.vinci.v3nlp.gate;

import org.apache.commons.validator.GenericValidator;

import gov.va.vinci.v3nlp.gate.HeaderVO;
import junit.framework.TestCase;


public class SimpleOperationsTest extends TestCase {
	
	public void testSimpleInstantiation() {
		HeaderVO c = new HeaderVO();
		junit.framework.Assert.assertNotNull("Null object.", c);
		
		
		String a = "";
		if (a != null && a.length() > 0) {
			
		}
		
		if (GenericValidator.isBlankOrNull(a)) {
			
		}
	}

}
