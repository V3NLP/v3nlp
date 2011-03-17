package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.PipeLine;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by IntelliJ IDEA.
 * User: ryancornia
 * Date: 3/16/11
 * Time: 9:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PipeLineProcessor {
    @Async
    void processPipeLine(String pipeLineId, PipeLine dataToProcess,
                         Corpus corpus);
}
