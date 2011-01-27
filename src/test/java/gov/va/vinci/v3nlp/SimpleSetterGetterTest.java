package gov.va.vinci.v3nlp;

import gov.va.vinci.test.GetterSetterTester;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.DocumentPedigreeCount;
import gov.va.vinci.v3nlp.model.operations.Sectionizer;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: ryancornia
 * Date: 1/27/11
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleSetterGetterTest extends TestCase {

    @Test
    public void testSimpleClass() {
        GetterSetterTester  tester = new GetterSetterTester();
        tester.testClass(CorpusSummary.class);
        tester.testClass(DocumentPedigreeCount.class);
        tester.testClass(LabelValue.class);
        tester.testClass(Sectionizer.class);
    }


    @Test
    public void testSpan() {
        Span s = new Span(10, 15);
        assert(s.getStart() == 10);
        assert(s.getEnd() == 15);
    }

    @Test
    public void testDocumentPedigreeCount() {
        DocumentPedigreeCount dc = new DocumentPedigreeCount("test", "pedigree", 10L);
        assertEquals(dc.getDocumentId(), "test");
        assertEquals(dc.getPedigree(), "pedigree");
        assert(dc.getCount().equals(10L));
    }
}
