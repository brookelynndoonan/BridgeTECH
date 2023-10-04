package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Career")
public class CareerRecord {

    private String id;

    private String careerName;
    private String location;
    private String jobDescription;
    private String companyDescription;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getCareerName() {
        return careerName;
    }

    @DynamoDBAttribute(attributeName = "location")
    public String getLocation() {
        return location;
    }

    @DynamoDBAttribute(attributeName = "jobdescription")
    public String getJobDescription() {
        return jobDescription;
    }

    @DynamoDBAttribute(attributeName = "companydescription")
    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerRecord that = (CareerRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
