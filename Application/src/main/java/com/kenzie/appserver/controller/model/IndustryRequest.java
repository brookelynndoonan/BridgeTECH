package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class IndustryRequest {
    @NotEmpty
    @JsonProperty("Industry Name")
    private String industryName;
    private String industryDescription;
    private String industryId;

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryDescription() {
        return industryDescription;
    }

    public void setIndustryDescription(String industryDescription) {
        this.industryDescription = industryDescription;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }
}
