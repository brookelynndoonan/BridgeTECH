package com.kenzie.appserver.service.model;

public class Career {

    private final String Id;
    private final String careerName;
    private final String location;
    private final String jobDescription;
    private final String companyDescription;

    public Career(String Id, String careerName, String location, String jobDescription, String companyDescription){

        this.Id = Id;
        this.careerName = careerName;
        this.location = location;
        this.jobDescription = jobDescription;
        this.companyDescription = companyDescription;
    }

    public String getId() {
        return Id;
    }

    public String getCareerName() {
        return careerName;
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
}
