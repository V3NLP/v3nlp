package gov.va.vinci.v3nlp.services.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "v3nlp.db_repository")
public class V3nlpDBRepository {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "repository_name")
    private String name;

    @Column(name = "repository_description")
    private String description;

    @Column
    private String url;

    @Column(name = "driver_class_name")
    private String driverClassName;

    @Column(name = "repository_schema")
    private String schema;

    @Column(name = "repository_table")
    private String table;

    @Column(name = "uid_column")
    private String uidColumn;

    @Column(name = "text_column")
    private String textColumn;

    @Column(name = "first_row")
    private Integer firstRow;

    @Column(name = "max_results")
    private Integer maxResults;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getUidColumn() {
        return uidColumn;
    }

    public void setUidColumn(String uidColumn) {
        this.uidColumn = uidColumn;
    }

    public String getTextColumn() {
        return textColumn;
    }

    public void setTextColumn(String textColumn) {
        this.textColumn = textColumn;
    }

    public Integer getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(Integer firstRow) {
        this.firstRow = firstRow;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public String getSelectSql() {
        if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(this.driverClassName)) {
            String selectQuery = "select " + uidColumn.replace("'", "''") + ", " +
                    textColumn.replace("'", "''")
                    + ", ROW_NUMBER() OVER (ORDER BY " + uidColumn.replace("'", "''") + ") as row " +
                    " from " + table.replace("'", "''");
            return "select * from (" + selectQuery + ") a where row <= " + maxResults;
        } else if ("com.mysql.jdbc.Driver".equals(driverClassName)) {
            return "select " + uidColumn.replace("'", "''") + ", " +
                    textColumn.replace("'", "''") +
                    " from " + table.replace("'", "''") +
                    " ORDER BY " + uidColumn.replace("'", "''") + " LIMIT " + maxResults;
        }

        return null;
    }
    
    public String getCountSql() {
        return "select  min(" + uidColumn.replace("'", "''") + "), min(" + textColumn.replace("'", "''") + "), count(*) from " + table.replace("'", "''");
    }
}
