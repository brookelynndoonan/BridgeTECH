package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyResponse;
import com.kenzie.appserver.repositories.CompanyRepository;
import com.kenzie.appserver.repositories.model.CompanyRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CompaniesService {
    private CompanyRepository companyRepository;

    public CompaniesService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyResponse> findAllCompanies() {

        List<CompanyRecord> recordList = StreamSupport.stream(companyRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(this::companyResponseFromRecord)
                .collect(Collectors.toList());
    }

    public CompanyResponse findCompanyById(String id) {

        return companyRepository
                .findById(id)
                .map(this::companyResponseFromRecord)
                .orElse(null);
    }

    public CompanyRecord findCompanyByName(String name) {

        if (name != null) {

            return companyRepository.findCompanyByName(name);
        } else {
            return null;
        }
    }

    public List<CompanyResponse> findAllCompaniesByName(String companyName) {
        List<CompanyRecord> recordList = companyRepository.findListOfCompaniesName(companyName);

        return recordList.stream()
                .map(this::companyResponseFromRecord)
                .collect(Collectors.toList());
    }

    public CompanyResponse addNewCompany(CompanyRequest companyRequest) {

        CompanyRecord companyRecord = companiesRecordFromRequest(companyRequest);

        companyRepository.save(companyRecord);

        return companyResponseFromRecord(companyRecord);


    }

    public CompanyResponse updateCompany(String id, String name, String description) {

        Optional<CompanyRecord> companyExist = companyRepository.findById(id);

        if (companyExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Not Found");
        }

        CompanyRecord record = companyExist.get();

        record.setCompanyName(name);
        record.setCompanyDescription(description);

        companyRepository.save(record);

        return companyResponseFromRecord(record);

    }


    public void deleteCompany(String id) {
        companyRepository.deleteById(id);
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
