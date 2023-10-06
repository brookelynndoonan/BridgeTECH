package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountsRequest {

    private String userName;
    private String accountType;
    private String password;
    @JsonProperty("Id")
    private String userId;
    private String email;

    public UserAccountsRequest(String userName, String accountType, String password, String userId, String email) {

        this.userName = userName;
        this.accountType = accountType;
        this.password = password;
        this.userId = userId;
        this.email = email;
    }

    public UserAccountsRequest() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserAccountsRequest{" +
                "userName='" + userName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

