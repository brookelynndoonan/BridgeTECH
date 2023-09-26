package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.IndustriesAndCompaniesDao;

import com.kenzie.capstone.service.model.IndustriesAndCompanies;
import com.kenzie.capstone.service.model.IndustriesAndCompaniesRecord;

import javax.inject.Inject;
import java.util.List;


public class IndustriesAndCompaniesService {

    private IndustriesAndCompaniesDao industriesAndCompaniesDao;

    @Inject
    public IndustriesAndCompaniesService(IndustriesAndCompaniesDao industriesAndCompaniesDao) {
        this.industriesAndCompaniesDao = industriesAndCompaniesDao;
    }

    public IndustriesAndCompanies getIndustriesAndCompanies(String name) {
        List<IndustriesAndCompaniesRecord> records = industriesAndCompaniesDao.getIndustriesAndCompanies(name);
        if (records.size() > 0) {
            return new IndustriesAndCompanies(records.get(0).getName(), records.get(0).getDescription());
        }
        return null;
    }

    public IndustriesAndCompanies setIndustriesAndCompanies(String name, String description) {
        IndustriesAndCompaniesRecord record = industriesAndCompaniesDao.setIndustriesAndCompanies(name, description);
        return new IndustriesAndCompanies(name, description);
    }
}
