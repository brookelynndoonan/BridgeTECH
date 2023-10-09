package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStoreCareer;
import com.kenzie.appserver.config.CacheStoreCompanies;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyResponse;
import com.kenzie.appserver.repositories.CompanyRepository;
import com.kenzie.appserver.repositories.model.CompanyRecord;
import com.kenzie.appserver.service.model.Career;
import com.kenzie.appserver.service.model.Companies;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {
    private CompanyRepository companyRepository;
    private CompaniesService companiesService;
    private CacheStoreCompanies cacheStore;

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        cacheStore = mock(CacheStoreCompanies.class);
        companiesService = new CompaniesService(companyRepository, cacheStore);
    }

    @Test
    void findByCompanyId() {
        // GIVEN
        String companyId = randomUUID().toString();

        CompanyRecord record = new CompanyRecord();
        record.setCompanyId(companyId);
        record.setCompanyName("companyName");
        record.setCompanyDescription("companyDescription");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(record));
        // WHEN
        Companies companies = companiesService.findByCompaniesId(companyId);

        // THEN
        Assertions.assertNotNull(companies, "The company is returned");
        Assertions.assertEquals(record.getCompanyId(), companies.getCompanyId(), "The company Id matches");
        Assertions.assertEquals(record.getCompanyName(), companies.getCompanyName(), "The company name matches");
        Assertions.assertEquals(record.getCompanyDescription(), companies.getCompanyDescription(), "The company description matches");
    }

    @Test
    void findByCompanyId_isNull_returnsNothing() {
        // GIVEN
        String companyId = randomUUID().toString();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
        // WHEN
        Companies companies = companiesService.findByCompaniesId(companyId);

        // THEN
        Assertions.assertNull(companies);
    }

    @Test
    void findByCompanyId_cacheNotNull_returnCachedCompany(){

            String companyId = randomUUID().toString();
            Companies companies = new Companies("companyName","companyDescription",
                    companyId);

            when(cacheStore.get(companyId)).thenReturn(companies);

            Companies actualCompanies = companiesService.findByCompaniesId(companyId);

            Assertions.assertEquals(companies, actualCompanies);
        }

    @Test
    void findAllCompanies_isValid_returnsListOfCompanies() {
        // GIVEN
        CompanyRecord record1 = new CompanyRecord();
        record1.setCompanyId(randomUUID().toString());
        record1.setCompanyName("companyName1");

        CompanyRecord record2 = new CompanyRecord();
        record2.setCompanyId(randomUUID().toString());
        record2.setCompanyName("companyName2");

        List<CompanyRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(companyRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<CompanyResponse> companies = companiesService.findAllCompanies();

        // THEN
        Assertions.assertNotNull(companies, "The companies list is returned");
        assertEquals(2, companies.size(), "There are two companies");

        for (CompanyResponse company : companies) {
            if (company.getCompanyId().equals(record1.getCompanyId())) {
                assertEquals(record1.getCompanyId(), company.getCompanyId(), "The company id matches");
                assertEquals(record1.getCompanyName(), company.getCompanyName(), "The company name matches");
            } else if (company.getCompanyId().equals(record2.getCompanyId())) {
                assertEquals(record2.getCompanyId(), company.getCompanyId(), "The company id matches");
                assertEquals(record2.getCompanyName(), company.getCompanyName(), "The company name matches");
            } else {
                Assertions.fail("Company returned that was not in the records!");
            }
        }
    }


    @Test
    void findCompanyByName_isValid_returnsCompany() {
        // GIVEN
        String companyName = "name";

        CompanyRecord record = new CompanyRecord();
        record.setCompanyName("Company Name");

        // WHEN
        when(companyRepository.findCompanyByName(companyName)).thenReturn(record);
        CompanyRecord company = companiesService.findCompanyByName(companyName);

        // THEN
        Assertions.assertNotNull(company, "The object is returned");
        assertEquals(record.getCompanyName(), company.getCompanyName(), "The name matches");
    }

    @Test
    void findCompanyByName_isInvalid_returnsNull() {

        when(companyRepository.findCompanyByName(null)).thenReturn(null);

        Assertions.assertNull(companiesService.findCompanyByName(null));

    }

    @Test
    void findAllCompaniesByName_isValid_returnsListOfCompanies() {

        List<CompanyRecord> companyRecords = new ArrayList<>();
        companyRecords.add(new CompanyRecord());
        when(companyRepository.findByCompanyName(anyString())).thenReturn(companyRecords);

        List<CompanyResponse> result = companiesService.findAllCompaniesByName("CompanyName");

        assertEquals(companyRecords.size(), result.size());
    }

    @Test
    void addNewCompany_isValid_companyIsAdded() {
        String companyName = "companyName";

        CompanyRequest request = new CompanyRequest();
        request.setCompanyName(companyName);

        ArgumentCaptor<CompanyRecord> companyRecordCaptor = ArgumentCaptor.forClass(CompanyRecord.class);

        // WHEN
        CompanyResponse returnedCompany = companiesService.addNewCompany(request);

        // THEN
        Assertions.assertNotNull(returnedCompany);

        verify(companyRepository).save(companyRecordCaptor.capture());

        CompanyRecord record = companyRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The company record is returned");
        Assertions.assertNotNull(record.getCompanyId(), "The company id exists");
        assertEquals(record.getCompanyName(), companyName, "The company name matches");
    }

    @Test
    void updateCompanyById_validId_ifIdExistsUpdateCompany(){
        // GIVEN
        String companyId = randomUUID().toString();

        Companies companies = new Companies(companyId, "companyDescription", companyId);

        ArgumentCaptor<CompanyRecord> companyRecordCaptor = ArgumentCaptor.forClass(CompanyRecord.class);

        // WHEN
        when(companyRepository.existsById(companyId)).thenReturn(true);
        companiesService.updateCompany(companies);

        // THEN
        verify(companyRepository).existsById(companyId);
        verify(companyRepository).save(companyRecordCaptor.capture());
        CompanyRecord companyRecord = companyRecordCaptor.getValue();

        Assertions.assertEquals(companies.getCompanyId(), companyRecord.getCompanyId());
        Assertions.assertEquals(companies.getCompanyName(), companyRecord.getCompanyName());
        Assertions.assertEquals(companies.getCompanyDescription(), companyRecord.getCompanyDescription());
    }

    @Test
    void updateCompanyById_ifIdNull_ifIdDoesNotExistReturnNull(){
        String companyId = randomUUID().toString();

        Companies companies = new Companies("companyName", "companyDescription",
                companyId);

        // WHEN
        when(companyRepository.existsById(companyId)).thenReturn(false);
        companiesService.updateCompany(companies);


        // THEN
        verify(companyRepository).existsById(companyId);
        verify(companyRepository, times(0)).save(any());

    }

    @Test
    void deleteCompany_isSuccessful() {
        String companyId = randomUUID().toString();

        // WHEN
        companiesService.deleteCompany(companyId);

        // THEN
        verify(companyRepository).deleteById(companyId);

    }
}
