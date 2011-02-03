package gov.va.vinci.v3nlp.model.datasources;

import gov.va.vinci.v3nlp.model.BaseNlpModule;

/**
 * Created by IntelliJ IDEA.
 * User: ryancornia
 * Date: 1/31/11
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryDataSource extends BaseNlpModule {

	private String dataDirectory;

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
}
