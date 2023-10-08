package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "LambdaUserAccounts")
public class UserAccountRecord {

    private String id;
    private String name;
    private String accountType;
    private String password;
    private String email;

    @DynamoDBHashKey(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Id")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "IdIndex", attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "userName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "accountType")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountRecord)) return false;
        UserAccountRecord that = (UserAccountRecord) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getAccountType(), that.getAccountType()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAccountType(), getPassword(), getEmail());
    }
}
