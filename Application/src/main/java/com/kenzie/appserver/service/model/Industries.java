package com.kenzie.appserver.service.model;

public class Industries {
    private final String industryName;
    private final String industryDescription;
    private final String industryId;

    public Industries(String industryName, String industryDescription, String industryId) {
        this.industryName = industryName;
        this.industryDescription = industryDescription;
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public String getIndustryDescription() {
        return industryDescription;
    }

    public String getIndustryId() {
        return industryId;
    }
}
