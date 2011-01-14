package gov.va.research.inlp;

import gov.va.research.inlp.model.PipeLine;
import gov.va.research.inlp.services.NegationImpl;
import gov.va.research.inlp.services.PipeLineProcessorImpl;
import gov.va.research.inlp.services.HitexGateModulesImpl;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.service.SerializationService;

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
	HitexGateModulesImpl sectionizerAndConceptFinder = null;


	@Getter
	@Setter
	private NegationImpl negationProvider;


	@Setter
	private String directoryToStoreResults;

	@Setter
	private PipeLineProcessorImpl pipeLineProcessor;
	
	@Setter
	private SerializationService serializationService;

	public void init() {
		if (!new File(directoryToStoreResults).exists()
				|| !new File(directoryToStoreResults).isDirectory()) {
			throw new RuntimeException(directoryToStoreResults
					+ ": Is not a valid directory to store results.");
		}
	}

	@Override
	public Corpus getPipeLineResults(String pipeLineId) {
		if (new File(directoryToStoreResults + pipeLineId + ".results")
				.exists()) {
			Corpus c = (Corpus) this.deSerialize(this.directoryToStoreResults
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
		return sectionizerAndConceptFinder.getAvailableSectionHeaders();
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
		return sectionizerAndConceptFinder.getDefaultSectionizerConfiguration();
	}

	
	@SneakyThrows
	private Object deSerialize(String path) {
		InputStream is = new FileInputStream(new File(path));
		ObjectInput oi = new ObjectInputStream(is);
		Object newObj = oi.readObject();
		oi.close();
		return newObj;
	}
	
	public String serializeCorpus(Corpus c)
	{
		return serializationService.serialize(c);
	}
	
	public Corpus deSerializeCorpus(String content)
	{
		return serializationService.deserialize(content, Corpus.class);
	}
}