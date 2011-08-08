package gov.va.vinci.v3nlp.expressionlib;

import gov.va.vinci.v3nlp.LabelValue;
import org.apache.commons.validator.GenericValidator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionServiceImpl {

	public static final List<LabelValue> SEARCHABLE_FIELDS = new ArrayList<LabelValue>();
	static {
		SEARCHABLE_FIELDS.add(new LabelValue("Expression", "expression"));
		SEARCHABLE_FIELDS.add(new LabelValue("Title", "title"));
		SEARCHABLE_FIELDS.add(new LabelValue("Description", "description"));
		SEARCHABLE_FIELDS.add(new LabelValue("Created By", "created_by"));
	}

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int validateExpression(String regEx, String testString) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(testString);
		boolean result = m.find();
		
		if (!result) {
			return -1;
		} else {
			return m.groupCount();
		}
	}

	public Collection<Expression> getAllExpressions() {
		Collection<Expression> expressions = this.jdbcTemplate
				.query(
						"select id, title, expression, description, created_by, created_date, matches, non_matches, citation from expressions ",
						new RowMapper<Expression>() {
							public Expression mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								Expression exp = new Expression();
								exp.setId(rs.getInt("id"));
								exp.setExpression(rs.getString("expression"));
								exp.setTitle(rs.getString("title"));
								exp.setDescription(rs.getString("description"));
								exp.setCreatedBy(rs.getString("created_by"));
								exp.setCreatedDate(rs.getDate("created_date"));
								exp.setCitation(rs.getString("citation"));
								exp.setMatches(rs.getString("matches"));
								exp.setNonMatches(rs.getString("non_matches"));
								return exp;
							}
						});
		return expressions;
	}

	public Collection<Expression> findExpressions(String haystack, String needle) {
		if (GenericValidator.isBlankOrNull(haystack)
				|| GenericValidator.isBlankOrNull(needle)) {
			throw new RuntimeException(
					"Haystack and needle must both have a value. ");
		}

		Collection<Expression> expressions = this.jdbcTemplate
				.query(
						"select id, title, expression, description, created_by, created_date, matches, non_matches, citation from expressions where "
								+ haystack
								+ " like '%"
								+ needle.replaceAll("'", "''") + "%'",
						new RowMapper<Expression>() {
							public Expression mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								Expression exp = new Expression();
								exp.setId(rs.getInt("id"));
								exp.setTitle(rs.getString("title"));
								exp.setExpression(rs.getString("expression"));
								exp.setDescription(rs.getString("description"));
								exp.setCreatedBy(rs.getString("created_by"));
								exp.setCreatedDate(rs.getDate("created_date"));
								exp.setCitation(rs.getString("citation"));
								exp.setMatches(rs.getString("matches"));
								exp.setNonMatches(rs.getString("non_matches"));
								return exp;
							}
						});
		return expressions;
	}

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
