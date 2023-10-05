package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountsRequest {

    private String userName;
    private String accountType;
    private String password;
    @JsonProperty("Id")
    private String userId;

    public UserAccountsRequest(String userName, String accountType, String password, String userId) {

        this.userName = userName;
        this.accountType = accountType;
        this.password = password;
        this.userId = userId;
    }

    public UserAccountsRequest() {
    }


    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = userName;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserAccountsRequest{" +
                "name='" + userName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

