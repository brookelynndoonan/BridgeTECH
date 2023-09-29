package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyResponse;
import com.kenzie.appserver.repositories.CompanyRepository;
import com.kenzie.appserver.repositories.model.CompanyRecord;
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

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        companiesService = new CompaniesService(companyRepository);
    }

    @Test
    void findCompanyById_isValid_returnsCompany() {
        // GIVEN
        String id = randomUUID().toString();

        CompanyRecord record = new CompanyRecord();
        record.setCompanyId(id);
        record.setCompanyName("Company Name");

        // WHEN
        when(companyRepository.findById(id)).thenReturn(Optional.of(record));
        CompanyResponse company = companiesService.findCompanyById(id);

        // THEN
        Assertions.assertNotNull(company, "The object is returned");
        assertEquals(record.getCompanyId(), company.getCompanyId(), "The id matches");
        assertEquals(record.getCompanyName(), company.getCompanyName(), "The name matches");
    }

    @Test
    void findByCompanyId_isInvalid_assertsNull() {
        // GIVEN
        String id = randomUUID().toString();

        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        CompanyResponse company = companiesService.findCompanyById(id);

        // THEN
        Assertions.assertNull(company, "The company is null when not found");
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
        // Arrange
        List<CompanyRecord> companyRecords = new ArrayList<>();
        companyRecords.add(new CompanyRecord());
        when(companyRepository.findListOfCompaniesName(anyString())).thenReturn(companyRecords);

        // Act
        List<CompanyResponse> result = companiesService.findAllCompaniesByName("CompanyName");

        // Assert
        assertEquals(companyRecords.size(), result.size());
        // You can add more assertions to verify the correctness of the mapping and response objects.
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
    void updateCareer_isValid_careerIsSuccessfullyUpdated() {
        // GIVEN
        String companyId = randomUUID().toString();

        CompanyRecord oldCompanyRecord = new CompanyRecord();
        oldCompanyRecord.setCompanyId(companyId);
        oldCompanyRecord.setCompanyName("oldCompanyName");
        oldCompanyRecord.setCompanyDescription("Gatekeep of the Bifröst between Earth and realm of the Gods");

        String newCompanyName = "newName";
        String newCompanyDescription = "newCompanyDescription";

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(oldCompanyRecord));

        ArgumentCaptor<CompanyRecord> companyRecordCaptor = ArgumentCaptor.forClass(CompanyRecord.class);

        // WHEN
        companiesService.updateCompany(companyId, newCompanyName, newCompanyDescription);

        // THEN
        verify(companyRepository).save(companyRecordCaptor.capture());

        CompanyRecord record = companyRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The company record is returned");
        assertEquals(record.getCompanyId(), companyId, "The company id matches");
        assertEquals(record.getCompanyName(), newCompanyName, "The company name matches");
        assertEquals(record.getCompanyDescription(), newCompanyDescription,
                "The company description matches");
    }

    @Test
    void updateCompany_does_not_exist() {
        // GIVEN
        String companyId = randomUUID().toString();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> companiesService.updateCompany(companyId,
                "newName", "newCompanyDescription"));

        // THEN
        try {
            verify(companyRepository, never()).save(Matchers.any());
        } catch (MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the company" +
                    " is not found in the database. - " + error);
        }
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
