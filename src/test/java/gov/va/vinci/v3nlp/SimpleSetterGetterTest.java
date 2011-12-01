/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp;

import gov.va.vinci.test.GetterSetterTester;
import gov.va.vinci.v3nlp.controller.ExpressionSearchForm;
import gov.va.vinci.v3nlp.expressionlib.Expression;
import gov.va.vinci.v3nlp.model.*;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import gov.va.vinci.v3nlp.model.datasources.TextDocument;
import gov.va.vinci.v3nlp.model.operations.BaseOperation;
import gov.va.vinci.v3nlp.model.operations.Concept;
import gov.va.vinci.v3nlp.model.operations.Negation;
import gov.va.vinci.v3nlp.model.operations.Sectionizer;
import gov.va.vinci.v3nlp.registry.*;
import gov.va.vinci.v3nlp.services.database.V3nlpDBRepository;
import junit.framework.TestCase;
import org.junit.Test;

public class SimpleSetterGetterTest extends TestCase {

    @Test
    public void testSimpleClass() {


        GetterSetterTester  tester = new GetterSetterTester();
        tester.testClass(BatchJobStatus.class);
        tester.testClass(Concept.class);
        tester.testClass(CorpusSummary.class);
        tester.testClass(DataServiceSource.class);
        tester.testClass(DocumentFeatureNameCount.class);
        tester.testClass(Expression.class);
        tester.testClass(ExpressionSearchForm.class);
        tester.testClass(LabelValue.class);
        tester.testClass(Negation.class);
        tester.testClass(NlpAnnotation.class);
        tester.testClass(NlpAnnotationAttribute.class);
        tester.testClass(NlpComponent.class);
        tester.testClass(NlpComponentCategory.class);
        tester.testClass(NlpComponentProvides.class);
        tester.testClass(NlpComponentRequires.class);
        tester.testClass(Sectionizer.class);
        tester.testClass(ServicePipeLine.class);
        tester.testClass(ServicePipeLineComponent.class);
        tester.testClass(StaticApplicationContext.class);
        tester.testClass(Template.class);
        tester.testClass(TextDocument.class);
        tester.testClass(V3nlpDBRepository.class);

        /** Abstract class, so create an inherited object to test base functionality. **/
        BaseNlpModule b = new BaseNlpModule() {
        };
        tester.testClass(b.getClass());
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
        DocumentFeatureNameCount dc = new DocumentFeatureNameCount("test", "pedigree", 10L);
        assertEquals(dc.getDocumentId(), "test");
        assertEquals(dc.getFeatureName(), "pedigree");
        assert(dc.getCount().equals(10L));
    }

    @Test
    public void testServicePipeLineComponent() {
        ServicePipeLineComponent comp = new ServicePipeLineComponent();
        assert(comp.toString().equals("ServiceUid: null -- Config: null"));

        comp.setServiceUid("1234");
        comp.setConfiguration("MyConfig");
        System.out.println(comp.toString());
        assert(comp.toString().equals("ServiceUid: 1234 -- Config: MyConfig"));
    }

    @Test
    public void testBaseOperation() {
        BaseOperation b = new BaseOperation() {

        };

        assert(b.isKeepFeatureInResult());
        b.setKeepFeatureInResult(false);
        assert(b.isKeepFeatureInResult() == false);
        b.setKeepFeatureInResult(true);
        assert(b.isKeepFeatureInResult());
    }


}
