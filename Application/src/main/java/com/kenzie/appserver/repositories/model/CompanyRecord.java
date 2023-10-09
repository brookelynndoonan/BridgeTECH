package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Companies")
public class CompanyRecord {

    private String companyName;
    private String companyDescription;
    private String companyId;


    @DynamoDBAttribute(attributeName = "companyName")
    public String getCompanyName() {
        return companyName;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getCompanyDescription() {
        return companyDescription;
    }

    @DynamoDBHashKey(attributeName = "Id")
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyRecord)) return false;
        CompanyRecord that = (CompanyRecord) o;
        return Objects.equals(getCompanyName(), that.getCompanyName())
                && Objects.equals(getCompanyDescription(), that.getCompanyDescription())
                && Objects.equals(getCompanyId(), that.getCompanyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompanyName(), getCompanyDescription(), getCompanyId());
    }
}