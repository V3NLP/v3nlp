package gov.va.research.inlp;

import gov.va.vinci.cm.DocumentInterface;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class VinciDocumentServiceImpl implements DocumentService {

	@SneakyThrows
	@Override
	public List<DocumentInterface> getRandomDocuments(Integer numberToGet) {

		List<DocumentInterface> docs = new ArrayList<DocumentInterface>();

		// TODO Implement.
		return docs;
	}
}
