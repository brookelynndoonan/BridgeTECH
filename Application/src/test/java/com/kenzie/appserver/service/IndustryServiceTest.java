package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class IndustryServiceTest {

    private IndustryRepository industryRepository;
    private IndustriesService industriesService;

    @BeforeEach
    void setup() {
        industryRepository = mock(IndustryRepository.class);
        industriesService = new IndustriesService(industryRepository);
    }

    @Test
    void findIndustryById_isValid_returnsIndustry() {
        // GIVEN
        String id = randomUUID().toString();

        IndustriesRecord record = new IndustriesRecord();
        record.setIndustryId(id);
        record.setIndustryName("Industry Name");

        // WHEN
        when(industryRepository.findById(id)).thenReturn(Optional.of(record));
        IndustryResponse industry = industriesService.findIndustryById(id);

        // THEN
        Assertions.assertNotNull(industry, "The object is returned");
        assertEquals(record.getIndustryId(), industry.getIndustryId(), "The id matches");
        assertEquals(record.getIndustryName(), industry.getIndustryName(), "The name matches");
    }

    @Test
    void findByIndustryId_isInvalid_assertsNull() {
        // GIVEN
        String id = randomUUID().toString();

        when(industryRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        IndustryResponse industry = industriesService.findIndustryById(id);

        // THEN
        Assertions.assertNull(industry, "The industry is null when not found");
    }

    @Test
    void findAllIndustries_isValid_returnsListOfIndustries() {
        // GIVEN
        IndustriesRecord record1 = new IndustriesRecord();
        record1.setIndustryId(randomUUID().toString());
        record1.setIndustryName("industryName1");

        IndustriesRecord record2 = new IndustriesRecord();
        record2.setIndustryId(randomUUID().toString());
        record2.setIndustryName("industryName2");

        List<IndustriesRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(industryRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<IndustryResponse> industries = industriesService.findAllIndustries();

        // THEN
        Assertions.assertNotNull(industries, "The industries list is returned");
        assertEquals(2, industries.size(), "There are two industries");

        for (IndustryResponse industry : industries) {
            if (industry.getIndustryId().equals(record1.getIndustryId())) {
                assertEquals(record1.getIndustryId(), industry.getIndustryId(), "The industry id matches");
                assertEquals(record1.getIndustryName(), industry.getIndustryName(), "The industry name matches");
            } else if (industry.getIndustryId().equals(record2.getIndustryId())) {
                assertEquals(record2.getIndustryId(), industry.getIndustryId(), "The industry id matches");
                assertEquals(record2.getIndustryName(), industry.getIndustryName(), "The industry name matches");
            } else {
                Assertions.fail("Industry returned that was not in the records!");
            }
        }
    }


    @Test
    void findIndustryByName_isValid_returnsIndustry() {
        // GIVEN
        String industryName = "name";

        IndustriesRecord record = new IndustriesRecord();
        record.setIndustryName("Industry Name");

        // WHEN
        when(industryRepository.findIndustryByName(industryName)).thenReturn(record);
        IndustriesRecord industry = industriesService.findIndustryByName(industryName);

        // THEN
        Assertions.assertNotNull(industry, "The object is returned");
        assertEquals(record.getIndustryName(), industry.getIndustryName(), "The name matches");
    }

    @Test
    void findIndustryByName_isInvalid_returnsNull() {

        when(industryRepository.findIndustryByName(null)).thenReturn(null);

        Assertions.assertNull(industriesService.findIndustryByName(null));

    }

    @Test
    void findAllIndustriesByName_isValid_returnsListOfIndustries() {
        List<IndustriesRecord> industryRecords = new ArrayList<>();
        industryRecords.add(new IndustriesRecord());
        when(industryRepository.findByIndustryName(anyString())).thenReturn(industryRecords);

        List<IndustryResponse> result = industriesService.findAllIndustriesByName("IndustryName");

        assertEquals(industryRecords.size(), result.size());
    }

    @Test
    void addNewIndustry_isValid_industryIsAdded() {
        String industryName = "industryName";

        IndustryRequest request = new IndustryRequest();
        request.setIndustryName(industryName);

        ArgumentCaptor<IndustriesRecord> industryRecordCaptor = ArgumentCaptor.forClass(IndustriesRecord.class);

        // WHEN
        IndustryResponse returnedIndustry = industriesService.addNewIndustry(request);

        // THEN
        Assertions.assertNotNull(returnedIndustry);

        verify(industryRepository).save(industryRecordCaptor.capture());

        IndustriesRecord record = industryRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The industry record is returned");
        Assertions.assertNotNull(record.getIndustryId(), "The industry id exists");
        assertEquals(record.getIndustryName(), industryName, "The industry name matches");
    }

    @Test
    void updateIndustry_isValid_industryIsSuccessfullyUpdated() {
        // GIVEN
        String industryId = randomUUID().toString();

        IndustriesRecord oldIndustryRecord = new IndustriesRecord();
        oldIndustryRecord.setIndustryId(industryId);
        oldIndustryRecord.setIndustryName("oldIndustryName");
        oldIndustryRecord.setIndustryDescription("Gatekeep of the Bifröst between Earth and realm of the Gods");

        String newIndustryName = "newName";
        String newIndustryDescription = "newIndustryDescription";

        when(industryRepository.findById(industryId)).thenReturn(Optional.of(oldIndustryRecord));

        ArgumentCaptor<IndustriesRecord> industryRecordCaptor = ArgumentCaptor.forClass(IndustriesRecord.class);

        // WHEN
        industriesService.updateIndustry(industryId, newIndustryName, newIndustryDescription);

        // THEN
        verify(industryRepository).save(industryRecordCaptor.capture());

        IndustriesRecord record = industryRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The industry record is returned");
        assertEquals(record.getIndustryId(), industryId, "The industry id matches");
        assertEquals(record.getIndustryName(), newIndustryName, "The industry name matches");
        assertEquals(record.getIndustryDescription(), newIndustryDescription,
                "The industries description matches");
    }

    @Test
    void updateIndustry_does_not_exist() {
        // GIVEN
        String industryId = randomUUID().toString();

        when(industryRepository.findById(industryId)).thenReturn(Optional.empty());

        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> industriesService.updateIndustry(industryId,
                "newName", "newIndustryDescription"));

        // THEN
        try {
            verify(industryRepository, never()).save(Matchers.any());
        } catch (MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the industry" +
                    " is not found in the database. - " + error);
        }
    }

    @Test
    void deleteIndustry_isSuccessful() {
        String industryId = randomUUID().toString();

        // WHEN
        industriesService.deleteIndustry(industryId);

        // THEN
        verify(industryRepository).deleteById(industryId);

    }
}
