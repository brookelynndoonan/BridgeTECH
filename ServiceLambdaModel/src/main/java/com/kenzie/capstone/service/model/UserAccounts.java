package com.kenzie.capstone.service.model;

import java.util.Objects;

public class UserAccounts {

    private String Id;
    private String name;
    private String accountType;
    private String password;
    private String email;

    public UserAccounts(String Id, String name, String accountType, String password
            , String email) {
        this.Id = Id;
        this.name = name;
        this.accountType = accountType;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccounts)) return false;
        UserAccounts that = (UserAccounts) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getAccountType(), that.getAccountType()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAccountType(), getPassword(), getEmail());
    }
}
