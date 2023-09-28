package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyResponse;
import com.kenzie.appserver.repositories.CompanyRepository;
import com.kenzie.appserver.repositories.model.CompanyRecord;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CompaniesService {
    private CompanyRepository companyRepository;

    public CompaniesService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyResponse> findAllCompanies() {

        List<CompanyRecord> recordList = StreamSupport.stream(companyRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(record -> companyResponseFromRecord(record))
                .collect(Collectors.toList());
    }

    public CompanyResponse findCompanyById(String id) {

        return companyRepository
                .findById(id)
                .map(this::companyResponseFromRecord)
                .orElse(null);
    }

    public List<CompanyResponse> findCompanyByName(String companyName) {
        List<CompanyRecord> recordList = companyRepository.findByCompanyName(companyName);

        return recordList.stream()
                .map(this::companyResponseFromRecord)
                .collect(Collectors.toList());
    }


    // PRIVATE HELPER METHODS
    private CompanyRecord companiesRecordFromRequest(CompanyRequest request) {

        CompanyRecord companyRecord = new CompanyRecord();

        companyRecord.setCompanyId(UUID.randomUUID().toString());
        companyRecord.setCompanyName(request.getCompanyName());
        companyRecord.setCompanyDescription(request.getCompanyDescription());


        return companyRecord;
    }

    private CompanyResponse companyResponseFromRecord(CompanyRecord companyRecord) {

        CompanyResponse companyResponse = new CompanyResponse();

        companyResponse.setCompanyId(companyRecord.getCompanyId());
        companyResponse.setCompanyName(companyRecord.getCompanyName());
        companyResponse.setCompanyDescription(companyRecord.getCompanyDescription());

        return companyResponse;
    }
}
