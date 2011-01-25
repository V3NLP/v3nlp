package gov.va.vinci.v3nlp;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.service.SerializationService;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.PipeLine;
import gov.va.vinci.v3nlp.services.NegationImpl;
import gov.va.vinci.v3nlp.services.PipeLineProcessorImpl;
import gov.va.vinci.v3nlp.services.SectionizerService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class DefaultNlpServiceImpl implements NlpService {


	@Getter
	@Setter
	private NegationImpl negationProvider;


	@Setter
	private String directoryToStoreResults;

	@Setter
	private PipeLineProcessorImpl pipeLineProcessor;
	
	@Setter
	private SerializationService serializationService;
	
	@Setter
	private SectionizerService sectionizerService;

	public void init() {
		if (!new File(directoryToStoreResults).exists()
				|| !new File(directoryToStoreResults).isDirectory()) {
			throw new RuntimeException(directoryToStoreResults
					+ ": Is not a valid directory to store results.");
		}
	}

	@Override
	public CorpusSummary getPipeLineResults(String pipeLineId) {
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
	@SneakyThrows
	public String submitPipeLine(PipeLine dataToProcess, Corpus corpus) {
		String pipeLineId = new Date().getTime() + "-"
				+ dataToProcess.hashCode() + "-" + corpus.hashCode();
		new File(directoryToStoreResults + pipeLineId + ".lck").createNewFile();
		pipeLineProcessor
				.processPipeLine(pipeLineId, dataToProcess, corpus);
		return pipeLineId;
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

	
	@SneakyThrows
	private Object deSerialize(String path) {
		InputStream is = new FileInputStream(new File(path));
		ObjectInput oi = new ObjectInputStream(is);
		Object newObj = oi.readObject();
		oi.close();
		return newObj;
	}
	
	public String serializeCorpus(CorpusSummary c)
	{
		return serializationService.serialize(c);
	}
	
	public CorpusSummary deSerializeCorpus(String content)
	{
		return serializationService.deserialize(content, CorpusSummary.class);
	}
}