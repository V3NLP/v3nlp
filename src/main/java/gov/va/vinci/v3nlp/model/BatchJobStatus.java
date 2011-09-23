package gov.va.vinci.v3nlp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name="batch_job_status")
public class BatchJobStatus {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    @Column(name="id")
    private Integer id;

    @Column
    private Date runDate;

    @Column
    private String status;

    @Column
    private String pipeLineId;

    @Column
    private String description;

    @Column
    private String jobName;

    @Column
    private String pipelineXml;

    @Column
    private String username;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPipelineXml() {
        return pipelineXml;
    }

    public void setPipelineXml(String pipelineXml) {
        this.pipelineXml = pipelineXml;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
