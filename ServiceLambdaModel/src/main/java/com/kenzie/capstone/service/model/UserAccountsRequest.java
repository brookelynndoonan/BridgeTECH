package com.kenzie.capstone.service.model;

public class UserAccountsRequest {

    private String name;
    private String accountType;
    private String password;
    private String userId;

    public UserAccountsRequest(String name, String accountType, String password, String userId) {

        this.name = name;
        this.accountType = accountType;
        this.password = password;
        this.userId = userId;
    }

    public UserAccountsRequest() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

