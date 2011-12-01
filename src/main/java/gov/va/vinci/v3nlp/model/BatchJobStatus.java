/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name="v3nlp.batch_job_status")
public class BatchJobStatus {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column
    private Date runDate;

    @Column
    private String status;

    @Column
    private String pipeLineId;

    @Column(length=8000)
    private String description;

    @Column(length=500)
    private String jobName;

    @Column(columnDefinition="TEXT")
    private String pipelineXml;

    @Column
    private String username;

    @Column
    private String resultPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }


}
