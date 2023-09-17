package com.kenzie.capstone.service.model;

public class UserAccountsRequest {

    private String name;
    private String accountType;
    private String password;

    public UserAccountsRequest(String name, String accountType, String password){

        this.name = name;
        this.accountType = accountType;
        this.password = password;
    }

    public UserAccountsRequest (){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "UserAccountsRequest{" +
                "name='" + name + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
