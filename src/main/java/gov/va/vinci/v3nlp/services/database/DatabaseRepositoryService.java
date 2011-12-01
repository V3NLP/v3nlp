/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.database;

import gov.va.vinci.cm.DocumentInterface;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepositoryService {

	public abstract List<DocumentInterface> getDocuments(V3nlpDBRepository m, String loggedInUser) throws SQLException;

    public abstract List<V3nlpDBRepository> getRepositories();
    
    public abstract String test(V3nlpDBRepository ds, String loggedInUser);
}