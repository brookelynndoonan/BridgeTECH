package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStoreCareer;
import com.kenzie.appserver.config.CacheStoreCompanies;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyResponse;
import com.kenzie.appserver.repositories.CompanyRepository;
import com.kenzie.appserver.repositories.model.CompanyRecord;
import com.kenzie.appserver.service.model.Companies;
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
    private final CacheStoreCompanies cacheStore;

    public CompaniesService(CompanyRepository companyRepository, CacheStoreCompanies cacheStore) {
        this.companyRepository = companyRepository;
        this.cacheStore = cacheStore;
    }

    public List<CompanyResponse> findAllCompanies() {

        List<CompanyRecord> recordList = StreamSupport.stream(companyRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(this::companyResponseFromRecord)
                .collect(Collectors.toList());
    }

    public Companies findByCompaniesId(String companyId) {
        Companies cachedCompanies = cacheStore.get(companyId);

        if (cachedCompanies != null) {
            return cachedCompanies;
        }

        Companies companiesFromBackendService = companyRepository
                .findById(companyId)
                .map(companies -> new Companies(companies.getCompanyName(),
                        companies.getCompanyDescription(), companyId))
                .orElse(null);


        if (companiesFromBackendService != null) {
            cacheStore.add(companiesFromBackendService.getCompanyId(), companiesFromBackendService);
        }

        return companiesFromBackendService;
    }

    public CompanyRecord findCompanyByName(String companyName) {

        if (companyName != null) {

            return companyRepository.findCompanyByName(companyName);
        } else {
            return null;
        }
    }

    public List<CompanyResponse> findAllCompaniesByName(String companyName) {
        List<CompanyRecord> recordList = companyRepository.findByCompanyName(companyName);

        return recordList.stream()
                .map(this::companyResponseFromRecord)
                .collect(Collectors.toList());
    }

    public CompanyResponse addNewCompany(CompanyRequest companyRequest) {

        CompanyRecord companyRecord = companiesRecordFromRequest(companyRequest);

        companyRepository.save(companyRecord);

        return companyResponseFromRecord(companyRecord);


    }

    public void updateCompany(Companies companies) {
        if (companyRepository.existsById(companies.getCompanyId())) {
            CompanyRecord companyRecord = new CompanyRecord();
            companyRecord.setCompanyId(companies.getCompanyId());
            companyRecord.setCompanyName(companies.getCompanyName());
            companyRecord.setCompanyDescription(companies.getCompanyDescription());
            companyRepository.save(companyRecord);
            cacheStore.evict(companyRecord.getCompanyId());
        }
    }


    public void deleteCompany(String companyId) {
        companyRepository.deleteById(companyId);
        cacheStore.evict(companyId);
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
