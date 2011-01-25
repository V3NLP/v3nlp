package gov.va.vinci.v3nlp.annotations;

import gov.va.vinci.cm.Annotation;
import junit.framework.TestCase;


public class SimpleOperationsTest extends TestCase {
	
	public void testSimpleInstantiation() {
		Annotation c = new Annotation(123);
		junit.framework.Assert.assertNotNull("Null object.", c);
		assert(c.getAnnotationId() ==123);
		
	}

}
