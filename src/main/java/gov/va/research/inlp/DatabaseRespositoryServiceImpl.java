package gov.va.research.inlp;

import gov.va.research.v3nlp.repo.DBRepository;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRespositoryServiceImpl {

	public List<DBRepository> repositories;

	public List<DBRepository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<DBRepository> repositories) {
		this.repositories = repositories;
	}
	
	public List<String> getRespostoryNames() {
		List<String> results = new ArrayList<String>();
		
		for (DBRepository d: repositories) {
			results.add(d.toString());
		}
		return results;
	}
	
}
