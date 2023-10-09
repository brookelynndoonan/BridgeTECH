package com.kenzie.appserver.service;

import com.kenzie.appserver.config.cachestore.CacheStoreIndustries;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import com.kenzie.appserver.service.model.Industries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IndustryServiceTest {
    private IndustryRepository industryRepository;
    private IndustriesService industryService;
    private CacheStoreIndustries cacheStore;

    @BeforeEach
    void setup() {
        industryRepository = mock(IndustryRepository.class);
        cacheStore = mock(CacheStoreIndustries.class);
        industryService = new IndustriesService(industryRepository, cacheStore);
    }

    @Test
    void findByIndustryId() {
        // GIVEN
        String industryId = randomUUID().toString();

        IndustriesRecord record = new IndustriesRecord();
        record.setIndustryId(industryId);
        record.setIndustryName("industryName");
        record.setIndustryDescription("industryDescription");

        when(industryRepository.findById(industryId)).thenReturn(Optional.of(record));
        // WHEN
        Industries industries = industryService.findByIndustriesId(industryId);

        // THEN
        Assertions.assertNotNull(industries, "The industry is returned");
        Assertions.assertEquals(record.getIndustryId(), industries.getIndustryId(), "The Industry Id matches");
        Assertions.assertEquals(record.getIndustryName(), industries.getIndustryName(), "The Industry name matches");
        Assertions.assertEquals(record.getIndustryDescription(), industries.getIndustryDescription(), "The Industry description matches");
    }

    @Test
    void findByIndustryId_isNull_returnsNothing() {
        // GIVEN
        String industryId = randomUUID().toString();

        when(industryRepository.findById(industryId)).thenReturn(Optional.empty());
        // WHEN
        Industries industries = industryService.findByIndustriesId(industryId);

        // THEN
        Assertions.assertNull(industries);
    }

    @Test
    void findByIndustryId_cacheNotNull_returnCachedIndustry() {

        String industryId = randomUUID().toString();
        Industries industries = new Industries("industryName", "industryDescription",
                industryId);

        when(cacheStore.get(industryId)).thenReturn(industries);

        Industries actualIndustries = industryService.findByIndustriesId(industryId);

        Assertions.assertEquals(industries, actualIndustries);
    }

    @Test
    void findAllIndustries_isValid_returnsListOfIndustries() {
        // GIVEN
        IndustriesRecord record1 = new IndustriesRecord();
        record1.setIndustryId(UUID.randomUUID().toString());
        record1.setIndustryName("industryName1");

        IndustriesRecord record2 = new IndustriesRecord();
        record2.setIndustryId(UUID.randomUUID().toString());
        record2.setIndustryName("industryName2");

        List<IndustriesRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(industryRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<IndustryResponse> industries = industryService.findAllIndustries();

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
    void findAllIndustriesByName_ValidIndustryName_ReturnsListOfIndustries() {
        IndustriesRecord record1 = new IndustriesRecord();
        record1.setIndustryId(UUID.randomUUID().toString());
        record1.setIndustryName("industryName1");

        IndustriesRecord record2 = new IndustriesRecord();
        record2.setIndustryId(UUID.randomUUID().toString());
        record2.setIndustryName("industryName2");

        List<IndustriesRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(industryRepository.findAll()).thenReturn(recordList);

        String industryNameToSearch = "industryName1";

        List<IndustryResponse> result = industryService.findAllIndustriesByName(industryNameToSearch);

        verify(industryRepository, times(1)).findAll();

        assertEquals(2, recordList.size());

        List<String> industryResponseNames = result.stream()
                .map(IndustryResponse::getIndustryName)
                .collect(Collectors.toList());

        assertTrue(industryResponseNames.contains("industryName1"));
        assertFalse(industryResponseNames.contains("industryName2"));
    }

    @Test
    void addNewIndustry_isValid_industryIsAdded() {
        String industryName = "industryName";

        IndustryRequest request = new IndustryRequest();
        request.setIndustryName(industryName);

        ArgumentCaptor<IndustriesRecord> industryRecordCaptor = ArgumentCaptor.forClass(IndustriesRecord.class);

        // WHEN
        IndustryResponse returnedIndustry = industryService.addNewIndustry(request);

        // THEN
        Assertions.assertNotNull(returnedIndustry);

        verify(industryRepository).save(industryRecordCaptor.capture());

        IndustriesRecord record = industryRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The industry record is returned");
        Assertions.assertNotNull(record.getIndustryId(), "The industry id exists");
        assertEquals(record.getIndustryName(), industryName, "The industry name matches");
    }

    @Test
    void updateIndustryById_validId_ifIdExistsUpdateIndustry() {
        // GIVEN
        String industryId = randomUUID().toString();

        Industries industries = new Industries("inustryName", "industryDescription",
                industryId);

        ArgumentCaptor<IndustriesRecord> industriesRecordCaptor = ArgumentCaptor.forClass(IndustriesRecord.class);

        // WHEN
        when(industryRepository.existsById(industryId)).thenReturn(true);
        industryService.updateIndustry(industries);

        // THEN
        verify(industryRepository).existsById(industryId);
        verify(industryRepository).save(industriesRecordCaptor.capture());
        IndustriesRecord industriesRecord = industriesRecordCaptor.getValue();

        Assertions.assertEquals(industries.getIndustryId(), industriesRecord.getIndustryId());
        Assertions.assertEquals(industries.getIndustryName(), industriesRecord.getIndustryName());
        Assertions.assertEquals(industries.getIndustryDescription(), industriesRecord.getIndustryDescription());
    }

    @Test
    void updateIndustryById_ifIdNull_ifIdDoesNotExistReturnNull() {
        String industryId = randomUUID().toString();

        Industries industries = new Industries("industryName", "industryDescription",
                industryId);

        // WHEN
        when(industryRepository.existsById(industryId)).thenReturn(false);
        industryService.updateIndustry(industries);


        // THEN
        verify(industryRepository).existsById(industryId);
        verify(industryRepository, times(0)).save(any());

    }

    @Test
    void deleteIndustry_isSuccessful() {
        String industryId = randomUUID().toString();

        // WHEN
        industryService.deleteIndustry(industryId);

        // THEN
        verify(industryRepository).deleteById(industryId);

    }
}

