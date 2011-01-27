package gov.va.vinci.v3nlp.services.hitex;

import gate.ProcessingResource;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * HitexTokenizerImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>01/27/2011</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/gate-context.xml", "/spring-beans.xml"})
public class HitexTokenizerImplTest extends TestCase {
    @Autowired
    private HitexTokenizerImpl tokenizerService;

    /**
     * Method: tokenize(gov.va.vinci.cm.Corpus _corpus)
     */
    @Test
    public void testTokenize()  {
        Corpus newCorpus = tokenizerService.tokenize(createSimpleCorpus());
        assert (newCorpus != null);
        assertEquals(newCorpus.getDocuments().get(0).getAnnotations().getAll().size(), 7);
    }

    /**
     * Method: tokenize(gov.va.vinci.cm.Corpus _corpus)
     */
    @Test
    public void testTokenizeException() {
        boolean foundException =false;
        ProcessingResource tempT = tokenizerService.tokenizer;
        tokenizerService.tokenizer = null;
        try {
            Corpus newCorpus = tokenizerService.tokenize(createSimpleCorpus());
        }  catch (RuntimeException e) {
            foundException = true;
        } finally {
            tokenizerService.tokenizer = tempT;
        }
        assert (foundException);
    }

    public static Corpus createSimpleCorpus() {

        Corpus c = new Corpus();
        Document d = new Document();
        d.setContent("This is a test");
        d.setDocumentId("test-doc-1");
        d.setDocumentName("test document");
        c.addDocument(d);
        return c;
    }

}
