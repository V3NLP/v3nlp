package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.service.SerializationService;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.services.NegationImpl;
import gov.va.vinci.v3nlp.services.PipeLineProcessor;
import gov.va.vinci.v3nlp.services.SectionizerService;

import java.io.*;
import java.util.Date;
import java.util.List;

public class DefaultNlpServiceImpl implements NlpService {

    private NegationImpl negationProvider;

    private String directoryToStoreResults;

    private PipeLineProcessor pipeLineProcessor;

    private SerializationService serializationService;

    private SectionizerService sectionizerService;


    public NegationImpl getNegationProvider() {
        return negationProvider;
    }

    public void setNegationProvider(NegationImpl negationProvider) {
        this.negationProvider = negationProvider;
    }

    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

    public void setPipeLineProcessor(PipeLineProcessor pipeLineProcessor) {
        this.pipeLineProcessor = pipeLineProcessor;
    }

    public void setSerializationService(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    public void setSectionizerService(SectionizerService sectionizerService) {
        this.sectionizerService = sectionizerService;
    }

    public void init() {
        if (!new File(directoryToStoreResults).exists()
                || !new File(directoryToStoreResults).isDirectory()) {
            throw new RuntimeException(directoryToStoreResults
                    + ": Is not a valid directory to store results.");
        }
    }

    @Override
    public CorpusSummary getPipeLineResults(String pipeLineId) {
        System.out.println("Getting file:" + directoryToStoreResults + pipeLineId + ".results");
        if (new File(directoryToStoreResults + pipeLineId + ".results")
                .exists()) {
            CorpusSummary c = (CorpusSummary) this.deSerialize(this.directoryToStoreResults
                    + pipeLineId + ".results");
            new File(directoryToStoreResults + pipeLineId + ".results")
                    .delete();
            return c;
        } else if (new File(directoryToStoreResults + pipeLineId + ".err")
                .exists()) {
            Exception e = (Exception) this
                    .deSerialize(this.directoryToStoreResults + pipeLineId
                            + ".err");
            new File(directoryToStoreResults + pipeLineId + ".err").delete();
            throw new RuntimeException(e);
        } else {
            throw new RuntimeException("Results not found.");
        }
    }

    @Override
    public String getPipeLineStatus(String pipeLineId) {
        System.out.println("Getting stats for:" + pipeLineId);

        System.out.println("Lock exists:" + directoryToStoreResults + pipeLineId + ".lck");
        if (new File(directoryToStoreResults + pipeLineId + ".lck").exists()) {
            return "PROCESSING";
        }
        if (new File(directoryToStoreResults + pipeLineId + ".err").exists()) {
            return "ERROR";
        }
        if (new File(directoryToStoreResults + pipeLineId + ".results").exists()) {
            return "COMPLETE";
        }

        return null;
    }

    @Override
    public String submitPipeLine(PipeLine dataToProcess, Corpus corpus) {
        try {
            String pipeLineId = new Date().getTime() + "-"
                    + dataToProcess.hashCode() + "-" + corpus.hashCode();
            new File(directoryToStoreResults + pipeLineId + ".lck").createNewFile();
            pipeLineProcessor
                    .processPipeLine(pipeLineId, dataToProcess, corpus);
            return pipeLineId;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAvailableSectionHeaders() {
        return sectionizerService.getAvailableSectionHeaders();
    }

    @Override
    public String getDefaultNegationConfiguration() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (String s : negationProvider.getDefaultNegationConfiguration()) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getDefaultSectionizerConfiguration() throws Exception {
        return sectionizerService.getDefaultSectionizerConfiguration();
    }

    private Object deSerialize(String path) {
        try {
            InputStream is = new FileInputStream(new File(path));
            ObjectInput oi = new ObjectInputStream(is);
            Object newObj = oi.readObject();
            oi.close();
            return newObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String serializeCorpus(Corpus c) {
        return serializationService.serialize(c);
    }

    public Corpus deSerializeCorpus(String content) {
        return serializationService.deserialize(content, Corpus.class);
    }

    public CorpusSummary deSerializeCorpusToCorpusSummary(String content) {
        return new CorpusSummary(deSerializeCorpus(content));
    }

    public String serializePipeline(PipeLine pipeLine) {
        return serializationService.serialize(pipeLine);
    }

    public PipeLine deserializePipeline(String content) {
        return serializationService.deserialize(content, PipeLine.class);
    }

    public String getPipeLineCasResult(String pipeLineId) {
        File aFile = new File(directoryToStoreResults + pipeLineId + ".results");
        if (aFile.exists()) {
            StringBuffer contents = null;
            BufferedReader input = null;

            try {
                input = new BufferedReader(new FileReader(aFile));
                String line = null;
                contents = new StringBuffer(); //not declared within while loop
                /*
                * readLine is a bit quirky :
                * it returns the content of a line MINUS the newline.
                * it returns null only for the END of the stream.
                * it returns an empty String if two newlines appear in a row.
                */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    input.close();
                } catch (Exception e) {
                }
            }
            new File(directoryToStoreResults + pipeLineId + ".results")
                    .delete();
            return contents.toString();
        } else if (new File(directoryToStoreResults + pipeLineId + ".err").exists()) {

            Exception e = (Exception) this
                    .deSerialize(this.directoryToStoreResults + pipeLineId
                            + ".err");
            new File(directoryToStoreResults + pipeLineId + ".err").delete();
            throw new RuntimeException(e);
        } else {
            throw new RuntimeException("Results not found.");
        }
    }
}