package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Industries")
public class IndustriesRecord {

    private String industryName;
    private String industryDescription;
    private String industryId;

    @DynamoDBAttribute(attributeName = "industryName")
    public String getIndustryName() {
        return industryName;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getIndustryDescription() {
        return industryDescription;
    }

    @DynamoDBHashKey(attributeName = "Id")
    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public void setIndustryDescription(String industryDescription) {
        this.industryDescription = industryDescription;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndustriesRecord)) return false;
        IndustriesRecord that = (IndustriesRecord) o;
        return Objects.equals(getIndustryName(), that.getIndustryName()) && Objects.equals(getIndustryDescription(), that.getIndustryDescription()) && Objects.equals(getIndustryId(), that.getIndustryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndustryName(), getIndustryDescription(), getIndustryId());
    }
}
