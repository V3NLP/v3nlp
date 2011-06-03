package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;

import java.util.Iterator;

public class CorpusController implements Runnable {
    private Corpus controller;
    private Iterator documentIterator;

    public CorpusController(Corpus c) {
        this.controller = c;
        documentIterator = c.getDocuments().iterator();
    }

    public void run() {
        Document report = null;
        while ((report = nextReport()) != null) {
                // TODO Do Processing.
        }
    }


    private Document nextReport() {
        synchronized (this.documentIterator) {
            if (this.documentIterator.hasNext())
                return (Document)this.documentIterator.next();
            else
                return null;
        }
    }
}

