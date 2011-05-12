package gov.va.vinci.v3nlp.services;

import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.Annotations;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.FormatInfo;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRespositoryServiceImpl implements
		DatabaseRepositoryService {

	// SQL statements
	public static final String UID_COLUMN_PARAMETER = ":uidColumn";
	public static final String TEXT_COLUMN_PARAMETER = ":textColumn";
	public static final String TABLE_REF_PARAMETER = ":tableRef";
	public static final String NO_FIRST_NO_LIMIT = "SELECT "
			+ UID_COLUMN_PARAMETER + " as uid, " + TEXT_COLUMN_PARAMETER
			+ " as text FROM " + TABLE_REF_PARAMETER + " ORDER BY "
			+ UID_COLUMN_PARAMETER;
	public static final String LIMITED_SQLSERVER_BASE = "WITH ["
			+ TABLE_REF_PARAMETER
			+ " ORDERED BY rnum] AS (SELECT row_number() over (order by "
			+ UID_COLUMN_PARAMETER + ") as rnum, " + UID_COLUMN_PARAMETER
			+ " as uid, " + TEXT_COLUMN_PARAMETER + " as text FROM "
			+ TABLE_REF_PARAMETER + ") SELECT * FROM [" + TABLE_REF_PARAMETER
			+ " ORDERED BY rnum]";
	public static final String NO_FIRST_YES_LIMIT_SQLSERVER = LIMITED_SQLSERVER_BASE
			+ " where rnum < ?";
	public static final String YES_FIRST_NO_LIMIT_SQLSERVER = LIMITED_SQLSERVER_BASE
			+ " where rnum >= ?";
	public static final String YES_FIRST_YES_LIMIT_SQLSERVER = LIMITED_SQLSERVER_BASE
			+ " where rnum >= ? and rnum < ?";
	public static final String NO_FIRST_YES_LIMIT_MYSQL = NO_FIRST_NO_LIMIT
			+ " LIMIT ?";
	public static final String YES_FIRST_NO_LIMIT_MYSQL = NO_FIRST_NO_LIMIT
			+ " LIMIT ?," + Long.MAX_VALUE;
	public static final String YES_FIRST_YES_LIMIT_MYSQL = NO_FIRST_NO_LIMIT
			+ " LIMIT ?,?";

	public List<DBRepository> repositories;

	public List<DBRepository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<DBRepository> repositories) {
		this.repositories = repositories;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.va.vinci.v3nlp.DatabaseRepositoryService#getRespostoryNames()
	 */
	public List<String> getRespostoryNames() {
		List<String> results = new ArrayList<String>();

		for (DBRepository d : repositories) {
			results.add(d.getName());
		}
		return results;
	}

	public List<DocumentInterface> getDocuments(DataServiceSource ds)
			throws SQLException {
		// FIX THIS! TODO Implement
        return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.va.research.v3nlp.repo.ReportRepository#getReports()
	 */
	private List<DocumentInterface> doGetDocuments(DBRepository repository,
			DataServiceSource ds) throws SQLException {
		boolean mysql = repository.getDriverClassName().contains("mysql");

		List<DocumentInterface> reportList = (ds.getNumberOfDocuments() != null && ds
				.getNumberOfDocuments() > 0) ? new ArrayList<DocumentInterface>(
				ds.getNumberOfDocuments())
				: new ArrayList<DocumentInterface>();
		String tableRef = (repository.getSchema() == null ? "" : repository
				.getSchema()
				+ ".")
				+ repository.getTable();
		String query = null;
		List<Integer> args = new ArrayList<Integer>();
		if (repository.getFirstRow() == null) {
			if (repository.getMaxResults() == null) {
				query = NO_FIRST_NO_LIMIT;
			} else {
				if (repository.getDriverClassName().contains("mysql")) {
					query = NO_FIRST_YES_LIMIT_MYSQL;
				} else {
					query = NO_FIRST_YES_LIMIT_SQLSERVER;
				}
				args.add(ds.getNumberOfDocuments());
			}
		} else {
			if (repository.getMaxResults() == null) {
				if (mysql) {
					query = YES_FIRST_NO_LIMIT_MYSQL;
				} else {
					query = YES_FIRST_NO_LIMIT_SQLSERVER;
				}
				args.add(repository.getFirstRow());
			} else {
				if (mysql) {
					query = YES_FIRST_YES_LIMIT_MYSQL;
				} else {
					query = YES_FIRST_YES_LIMIT_SQLSERVER;
				}
				args.add(repository.getFirstRow());
				args.add(repository.getFirstRow() + ds.getNumberOfDocuments());
			}
		}
		query = query.replace(UID_COLUMN_PARAMETER,
				repository.getUidColumn()).replace(
				TEXT_COLUMN_PARAMETER, repository.getTextColumn())
				.replace(TABLE_REF_PARAMETER, tableRef);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			try {
				Class.forName(repository.getDriverClassName());
			} catch (Exception e) {
				throw new RuntimeException(
						"Could not find database driver for driver class:"
								+ repository.getDriverClassName());
			}

			// Set the username and password on the driver url.
			String connectionUrl = repository.getUrl();
			if (ds.getDataServiceUsername() != null) {
				connectionUrl = connectionUrl.replace("{username}", ds
						.getDataServiceUsername());
			}
			if (ds.getDataServicePassword() != null) {
				connectionUrl = connectionUrl.replace("{password}", ds
						.getDataServicePassword());
			}
			conn = DriverManager.getConnection(connectionUrl);
			ps = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < args.size(); i++) {
				ps.setInt(i + 1, args.get(i).intValue());
			}

			rs = ps.executeQuery();

			boolean validRow = rs.next();
			while (validRow) {
				reportList.add(new Document("Repository: "
						+ rs.getString("uid"), rs.getString("uid"), null,
						new ArrayList<FormatInfo>(), rs.getString("text"),
						new Annotations()));
				validRow = rs.next();
			}

			return reportList;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {

			}
			try {
				ps.close();
			} catch (Exception e) {

			}
			try {
				conn.close();
			} catch (Exception e) {

			}
		}
	}

}
