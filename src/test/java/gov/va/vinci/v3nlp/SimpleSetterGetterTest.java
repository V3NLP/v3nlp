package gov.va.vinci.v3nlp;

import gov.va.vinci.test.GetterSetterTester;
import gov.va.vinci.v3nlp.controller.ExpressionSearchForm;
import gov.va.vinci.v3nlp.expressionlib.Expression;
import gov.va.vinci.v3nlp.gate.HeaderVO;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.DocumentPedigreeCount;
import gov.va.vinci.v3nlp.model.Template;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import gov.va.vinci.v3nlp.model.datasources.TextDocument;
import gov.va.vinci.v3nlp.model.operations.Concept;
import gov.va.vinci.v3nlp.model.operations.Sectionizer;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentCategory;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.registry.NlpComponentRequires;
import junit.framework.TestCase;
import org.junit.Test;

public class SimpleSetterGetterTest extends TestCase {

    @Test
    public void testSimpleClass() {
        GetterSetterTester  tester = new GetterSetterTester();
        tester.testClass(Concept.class);
        tester.testClass(CorpusSummary.class);
        tester.testClass(DataServiceSource.class);
        tester.testClass(DocumentPedigreeCount.class);
        tester.testClass(Expression.class);
        tester.testClass(ExpressionSearchForm.class);
        tester.testClass(HeaderVO.class);
        tester.testClass(LabelValue.class);
        tester.testClass(NlpComponent.class);
        tester.testClass(NlpComponentCategory.class);
        tester.testClass(NlpComponentProvides.class);
        tester.testClass(NlpComponentRequires.class);
        tester.testClass(Sectionizer.class);
        tester.testClass(Template.class);
        tester.testClass(TextDocument.class);
    }


    @Test
    public void testSpan() {
        Span s = new Span(10, 15);
        assert(s.getStart() == 10);
        assert(s.getEnd() == 15);

        s.setStart(20);
        s.setEnd(30);
        assert(s.getStart() == 20);
        assert(s.getEnd() == 30);
    }

    @Test
    public void testDocumentPedigreeCount() {
        DocumentPedigreeCount dc = new DocumentPedigreeCount("test", "pedigree", 10L);
        assertEquals(dc.getDocumentId(), "test");
        assertEquals(dc.getPedigree(), "pedigree");
        assert(dc.getCount().equals(10L));
    }
}
