/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model;


import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.services.database.V3nlpDBRepository;

import java.util.*;

public class ServicePipeLine {
    private List<ServicePipeLineComponent> services = new ArrayList<ServicePipeLineComponent>();
    private String pipeLineName = "";
    private String userToken = "";
    private String description;
    private Corpus corpus;
    private Date createdDate;
    private Integer numberOfProcesses;
    private String jobName;
    private String jobDescription;
    private String pipelineXml;
    private List<V3nlpDBRepository> saveToRepositories;
    private List<String> resultsSettings;

    public List<ServicePipeLineComponent> getServices() {
        return services;
    }

    public void setServices(List<ServicePipeLineComponent> services) {
        this.services = services;
    }

    public String getPipeLineName() {
        return pipeLineName;
    }

    public void setPipeLineName(String pipeLineName) {
        this.pipeLineName = pipeLineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Integer getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public void setNumberOfProcesses(Integer numberOfProcesses) {
        this.numberOfProcesses = numberOfProcesses;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }


    public String getPipelineXml() {
        return pipelineXml;
    }

    public void setPipelineXml(String pipelineXml) {
        this.pipelineXml = pipelineXml;
    }

    public List<V3nlpDBRepository> getSaveToRepositories() {
        return saveToRepositories;
    }

    public void setSaveToRepositories(List<V3nlpDBRepository> saveToRepositories) {
        this.saveToRepositories = saveToRepositories;
    }

    public List<String> getResultsSettings() {
        return resultsSettings;
    }

    public void setResultsSettings(List<String> resultsSettings) {
        this.resultsSettings = resultsSettings;
    }

    public String toString() {
        String toReturn = "PipeLine: " + pipeLineName + " - " + this.description +
                " [" + this.createdDate + "] Number of processes: " + numberOfProcesses + "\n" +
                "Services:\n\t";
        for (ServicePipeLineComponent sp : this.services) {
            toReturn += sp + "\n\t";
        }
        return toReturn;
    }



}
