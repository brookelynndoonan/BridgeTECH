package com.kenzie.appserver.service;

import com.kenzie.appserver.config.cachestore.CacheStoreIndustries;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import com.kenzie.appserver.service.model.Industries;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IndustriesService {
    private IndustryRepository industryRepository;
    private final CacheStoreIndustries cacheStore;

    public IndustriesService(IndustryRepository industryRepository, CacheStoreIndustries cacheStore) {
        this.industryRepository = industryRepository;
        this.cacheStore = cacheStore;
    }

    public List<IndustryResponse> findAllIndustries() {

        List<IndustriesRecord> recordList = StreamSupport.stream(industryRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(this::industryResponseFromRecord)
                .collect(Collectors.toList());
    }

    public Industries findByIndustriesId(String Id) {
        Industries cachedIndustries = cacheStore.get(Id);
        if (cachedIndustries != null) {
            return cachedIndustries;
        }
        Industries industriesFromBackendService = industryRepository
                .findById(Id)
                .map(industry -> new Industries(industry.getIndustryName(), industry.getIndustryDescription(),
                        industry.getIndustryId()))
                .orElse(null);

        if (industriesFromBackendService != null) {
            cacheStore.add(industriesFromBackendService.getIndustryId(), industriesFromBackendService);
        }
        return industriesFromBackendService;
    }

    //Blake helped me write this method.
    public List<IndustryResponse> findAllIndustriesByName(String industryName) {
        Iterable<IndustriesRecord> recordList = industryRepository.findAll();

        List<IndustriesRecord> industryList = new ArrayList<>();
        for (IndustriesRecord industryRecord : recordList) {
            if (industryRecord.getIndustryName().equals(industryName)) {
                industryList.add(industryRecord);
            }
        }
        return industryList.stream()
                .map(this::industryResponseFromRecord)
                .collect(Collectors.toList());
    }

    public IndustryResponse addNewIndustry(IndustryRequest industryRequest) {
        IndustriesRecord industryRecord = industriesRecordFromRequest(industryRequest);
        industryRepository.save(industryRecord);
        return industryResponseFromRecord(industryRecord);
    }

    public void updateIndustry(Industries industries) {
        if (industryRepository.existsById(industries.getIndustryId())) {
            IndustriesRecord industriesRecord = new IndustriesRecord();
            industriesRecord.setIndustryId(industries.getIndustryId());
            industriesRecord.setIndustryName(industries.getIndustryName());
            industriesRecord.setIndustryDescription(industries.getIndustryDescription());
            industryRepository.save(industriesRecord);
            cacheStore.evict(industriesRecord.getIndustryId());
        }
    }

    public void deleteIndustry(String industryId) {
        industryRepository.deleteById(industryId);
        cacheStore.evict(industryId);
    }

    // PRIVATE HELPER METHODS
    private IndustriesRecord industriesRecordFromRequest(IndustryRequest request) {
        IndustriesRecord industriesRecord = new IndustriesRecord();

        industriesRecord.setIndustryId(UUID.randomUUID().toString());
        industriesRecord.setIndustryName(request.getIndustryName());
        industriesRecord.setIndustryDescription(request.getIndustryDescription());

        return industriesRecord;
    }

    private IndustryResponse industryResponseFromRecord(IndustriesRecord industryRecord) {

        IndustryResponse industryResponse = new IndustryResponse();

        industryResponse.setIndustryId(industryRecord.getIndustryId());
        industryResponse.setIndustryName(industryRecord.getIndustryName());
        industryResponse.setIndustryDescription(industryRecord.getIndustryDescription());

        return industryResponse;
    }
}
