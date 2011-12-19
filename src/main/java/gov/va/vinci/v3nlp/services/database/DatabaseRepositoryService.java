/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.database;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepositoryService {

	public abstract List<DocumentInterface> getDocuments(V3nlpDBRepository m, String loggedInUser) throws SQLException;

    public abstract List<V3nlpDBRepository> getRepositories();

    /**
     * Given a dataservice and logged in user name, test the
     * service.
     *
     * @param ds Dataservice to test.
     * @param loggedInUser User logged in.
     * @return  An empty string if successful, or the text of the
     *  error message if unsuccessful.
     */
    public abstract String test(V3nlpDBRepository ds, String loggedInUser);

    /**
     * Given a dataservice and logged in user name, test the
     * service. This needs to insure the proper schema is available
     * to save results into.
     *
     * @param ds Dataservice to test.
     * @param loggedInUser User logged in.
     * @return  An empty string if successful, or the text of the
     *  error message if unsuccessful.
     */
    public abstract String testForSave(V3nlpDBRepository ds, String loggedInUser);
    
    
    public abstract void writeCorpus(Corpus c, V3nlpDBRepository ds, String loggedInUser);
}