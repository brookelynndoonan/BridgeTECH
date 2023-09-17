package com.kenzie.capstone.service.model;

import java.util.Objects;

public class UserAccounts {

    private String id;
    private String name;
    private String accountType;
    private String password;

    public UserAccounts (String id, String name, String accountType, String password){
        this.id = id;
        this.name = name;
        this.accountType = accountType;
        this.password = password;
    }

    public UserAccounts(){}

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccounts that = (UserAccounts) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
