package gov.va.research.inlp.gate;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.w3c.dom.Document;

import gate.creole.ExecutionException;
import gov.va.research.inlp.gate.SectionizerHeaderFactory;
import hitex.util.Header;

public class SectionizerHeaderFactoryTest extends TestCase {

	@Test
	public void testNullDoc() {
		try {
			SectionizerHeaderFactory.getHeaders((org.w3c.dom.Document) null);
		} catch (ExecutionException e) {
			return;
		}
		fail("Should have thrown an exception.");
	}

	@Test
	public void testNullRoot() {
		try {
			// TODO FIX THIS! Create a mock.
			// Document d = new Document();
			SectionizerHeaderFactory.getHeaders((org.w3c.dom.Document) null);
		} catch (ExecutionException e) {
			return;
		}
		fail("Should have thrown an exception.");
	}

	@Test
	public void testNullFile() throws Exception {
		try {
			SectionizerHeaderFactory.getHeaders((File) null);
		} catch (ExecutionException e) {
			return;
		}
		fail("Should have thrown an exception.");
	}



	@Test
	public void testWithJunkFile() throws Exception {
		try {
			File f= new File("--junk--");
			SectionizerHeaderFactory.getHeaders(f);
		} catch (ExecutionException e) {
			return;
		}
		
		junit.framework.Assert.fail("Should not have thrown an exception.");		
	}
	
	@Test
	public void testWithValidFile() throws Exception {
		List<Header> results = null;
		try {
			File f= new File("src/test/resources/dis_headers.xml");
			results = SectionizerHeaderFactory.getHeaders(f);
		} catch (ExecutionException e) {
			junit.framework.Assert.fail("Should not have thrown an exception.");
		}
		
		assertEquals(871, results.size());		
	}

	@Test
	public void testInvalidXML() throws Exception {
		try {			
			SectionizerHeaderFactory.parseXmlFile("JUNK!",  true);
		} catch (ExecutionException e) {
			return;
		}
		junit.framework.Assert.fail("Should have thrown an exception.");
		
	}
	
	@Test
	public void testNoCategoryXML() throws Exception {
		String tXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
					  "<headers>\n" +
					  "<header captGroupNum=\"0\" >" +
					  "    <![CDATA[(?i)ABDOMINAL\\s{0,3}(:|;)]]>\n" +
					  "</header>\n" +
					  "</headers>";
		Document d = SectionizerHeaderFactory.parseXmlFile(tXml,  false);
		try {
			SectionizerHeaderFactory.getHeaders(d);
		} catch (Exception e) {
			return;
		}
		junit.framework.Assert.fail("Should have thrown an exception, no Categories in XML.");
	}

	@Test
	public void testNoCaptGroupNum() throws Exception {
		String tXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
					  "<headers>\n" +
					  "<header categories=\"test\"  >" +
					  "    <![CDATA[(?i)ABDOMINAL\\s{0,3}(:|;)]]>\n" +
					  "</header>\n" +
					  "</headers>";
		Document d = SectionizerHeaderFactory.parseXmlFile(tXml,  false);
		try {
			SectionizerHeaderFactory.getHeaders(d);
		} catch (Exception e) {
			return;
		}
		junit.framework.Assert.fail("Should have thrown an exception, no CaptGroupNum in XML.");
	}
	
	@Test
	public void testBadCategoryNameXML() throws Exception {
		String tXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
					  "<headers>\n" +
					  "<header categories=\"\" captGroupNum=\"0\" >" +
					  "    <![CDATA[(?i)ABDOMINAL\\s{0,3}(:|;)]]>\n" +
					  "</header>\n" +
					  "</headers>";
		try {
			 SectionizerHeaderFactory.getHeaders(SectionizerHeaderFactory.parseXmlFile(tXml, false));
		} catch (Exception e) {
			return;		
		}
		junit.framework.Assert.fail("Should have thrown an exception, no Category name in XML.");
	}
}
