package com.kenzie.capstone.service.model;

import java.util.Objects;

public class IndustriesAndCompanies {

    private String name;
    private String description;

    public IndustriesAndCompanies(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public IndustriesAndCompanies() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndustriesAndCompanies)) return false;
        IndustriesAndCompanies that = (IndustriesAndCompanies) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }
}
