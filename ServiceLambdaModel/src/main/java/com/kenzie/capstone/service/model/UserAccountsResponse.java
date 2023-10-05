package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountsResponse {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("userName")
    private String userName;
    private String accountType;
    private String password;

    public UserAccountsResponse(String id, String userName, String accountType, String password) {
        this.id = id;
        this.userName = userName;
        this.accountType = accountType;
        this.password = password;
    }

    public UserAccountsResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountType() {
        return accountType;
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

    @Override
    public String toString() {
        return "UserAccountsResponse{" +
                "Id='" + id + '\'' +
                ", name='" + userName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
