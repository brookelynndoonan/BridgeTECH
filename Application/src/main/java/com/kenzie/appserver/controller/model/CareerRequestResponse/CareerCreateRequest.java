package com.kenzie.appserver.controller.model.CareerRequestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class CareerCreateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;
    @NotEmpty
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("location")
    private String location;
    @JsonProperty("jobDescription")
    private String jobDescription;
    @JsonProperty("companyDescription")
    private String companyDescription;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

}
