package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class CareerCreateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;
    private String id;
    private String location;
    private String jobDescription;
    private String companyDescription;

    public String getName() {
        return name;
    }
    public String getId() { return id; }
    public String getLocation() {
        return location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getCompanyDescription() {
        return companyDescription;
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
