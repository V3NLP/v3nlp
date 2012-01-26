package gov.va.vinci.v3nlp.services.gate;


import gate.ProcessingResource;
import gate.util.GateException;
import org.junit.Test;

import java.net.MalformedURLException;

public class GenericGateServiceTest {

    ProcessingResource resource = new hitex.gate.SentenceSplitter();

    @Test
    public void testConstructor() {
//        GenericGateService service = new GenericGateService(resource);
    }

    @Test
    public void testProcess() throws GateException, MalformedURLException {
        /**
         *
          *
        Gate.setGateHome(new File("/Users/vhaislcornir/git/v3nlp-server/src/main/webapp"));
        Gate.setSiteConfigFile(new File("/Users/vhaislcornir/git/v3nlp-server/src/main/webapp/WEB-INF/config/gate.xml"));
        Gate.setPluginsHome(new File("/Users/vhaislcornir/git/v3nlp-server/src/main/webapp/WEB-INF/resources"));
        Gate.addAutoloadPlugin(new URL("file://Users/vhaislcornir/git/v3nlp-server/src/main/webapp/WEB-INF/plugins/ANNIE/ANNIE_with_defaults.gapp"));
        Gate.init();
        ProcessingResource resource2 = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");

        GenericGateService service = new GenericGateService(resource2);
        Document d = new Document();
        d.setDocumentId("1");
        d.setContent("This is a test.");
        service.process("My Config", d, new ArrayList<NlpComponent>());
         */

    }


}
