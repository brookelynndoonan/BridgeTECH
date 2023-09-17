package com.kenzie.capstone.service.model;

public class UserAccountsResponse {

    private String id;
    private String name;
    private String accountType;
    private String password;

    public UserAccountsResponse(String id, String name, String accountType, String password){
        this.id = id;
        this.name = name;
        this.accountType = accountType;
        this.password = password;
    }

    public UserAccountsResponse (){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
