package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountsResponse {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("userName")
    private String userName;
    private String accountType;
    private String password;
    private String email;

    public UserAccountsResponse(String id, String userName, String accountType, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.accountType = accountType;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserAccountsResponse{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
