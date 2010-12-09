package gov.va.research.inlp;

import gov.va.research.inlp.model.datasources.DataServiceSource;
import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.FormatInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRespositoryServiceImpl implements
		DatabaseRepositoryService {

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
	 * @see gov.va.research.inlp.DatabaseRepositoryService#getRespostoryNames()
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
		DBRepository repository = null;
		for (DBRepository d : repositories) {
			if (d.getName().equals(ds.getDataServiceName())) {
				repository = d;
				break;
			}
		}
		if (repository == null) {
			throw new RuntimeException("Repository " + ds.getDataServiceName()
					+ " not configured.");
		}

		return doGetDocuments(repository, ds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.va.research.v3nlp.repo.ReportRepository#getReports()
	 */
	private List<DocumentInterface> doGetDocuments(DBRepository repository,
			DataServiceSource ds) throws SQLException {
		boolean mysql = repository.isMysql();

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
				query = DBRepository.NO_FIRST_NO_LIMIT;
			} else {
				if (repository.isMysql()) {
					query = DBRepository.NO_FIRST_YES_LIMIT_MYSQL;
				} else {
					query = DBRepository.NO_FIRST_YES_LIMIT_SQLSERVER;
				}
				args.add(ds.getNumberOfDocuments());
			}
		} else {
			if (repository.getMaxResults() == null) {
				if (mysql) {
					query = DBRepository.YES_FIRST_NO_LIMIT_MYSQL;
				} else {
					query = DBRepository.YES_FIRST_NO_LIMIT_SQLSERVER;
				}
				args.add(repository.getFirstRow());
			} else {
				if (mysql) {
					query = DBRepository.YES_FIRST_YES_LIMIT_MYSQL;
				} else {
					query = DBRepository.YES_FIRST_YES_LIMIT_SQLSERVER;
				}
				args.add(repository.getFirstRow());
				args.add(repository.getFirstRow() + ds.getNumberOfDocuments());
			}
		}
		query = query.replace(DBRepository.UID_COLUMN_PARAMETER,
				repository.getUidColumn()).replace(
				DBRepository.TEXT_COLUMN_PARAMETER, repository.getTextColumn())
				.replace(DBRepository.TABLE_REF_PARAMETER, tableRef);

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
			connectionUrl = connectionUrl.replace("{username}", ds
					.getDataServiceUsername());
			connectionUrl = connectionUrl.replace("{password}", ds
					.getDataServicePassword());
			conn = DriverManager.getConnection(connectionUrl);
			ps = conn.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < args.size(); i++) {
				ps.setInt(i + 1, args.get(i).intValue());
			}

			rs = ps.executeQuery();

			boolean validRow = rs.next();
			while (validRow) {
				reportList
						.add(new Document("Repository: " + rs.getString("uid"),
								rs.getString("uid"), null,
								new ArrayList<FormatInfo>(), rs
										.getString("text"), null));
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
