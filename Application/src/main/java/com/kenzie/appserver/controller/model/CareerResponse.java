package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareerResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("jobdescription")
    private String jobDescription;

    @JsonProperty("companydescription")
    private String companyDescription;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
}
