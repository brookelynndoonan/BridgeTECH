package com.kenzie.appserver.service.model;

public class Companies {
    private final String companyName;
    private final String companyDescription;
    private final String companyId;

    public Companies(String companyName, String companyDescription, String companyId) {
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getCompanyId() {
        return companyId;
    }
}
