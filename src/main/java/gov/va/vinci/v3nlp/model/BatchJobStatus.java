package gov.va.vinci.v3nlp.model;

import java.util.Date;

public class BatchJobStatus {
    private Date runDate;
    private String status;
    private String pipeLineId;

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPipeLineId() {
        return pipeLineId;
    }

    public void setPipeLineId(String pipeLineId) {
        this.pipeLineId = pipeLineId;
    }
}
