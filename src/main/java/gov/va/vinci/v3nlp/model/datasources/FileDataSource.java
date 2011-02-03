package gov.va.vinci.v3nlp.model.datasources;

import gov.va.vinci.v3nlp.model.BaseNlpModule;

/**
 * Created by IntelliJ IDEA.
 * User: ryancornia
 * Date: 1/31/11
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileDataSource extends BaseNlpModule {

	private String dataFileName;

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }
}
