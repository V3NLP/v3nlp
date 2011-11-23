package gov.va.vinci.v3nlp.model.datasources;

import gov.va.vinci.v3nlp.model.BaseNlpModule;


public class DataServiceSource extends BaseNlpModule {
    private Integer numberOfDocuments;
    private String dataServiceUsername;
    private String databaseServer;
    private String database;
    private String table;
    private String idColumn;
    private String textColumn;

    public Integer getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(Integer numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public String getDataServiceUsername() {
        return dataServiceUsername;
    }

    public void setDataServiceUsername(String dataServiceUsername) {
        this.dataServiceUsername = dataServiceUsername;
    }

    public String getDatabaseServer() {
        return databaseServer;
    }

    public void setDatabaseServer(String databaseServer) {
        this.databaseServer = databaseServer;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getTextColumn() {
        return textColumn;
    }

    public void setTextColumn(String textColumn) {
        this.textColumn = textColumn;
    }

}
