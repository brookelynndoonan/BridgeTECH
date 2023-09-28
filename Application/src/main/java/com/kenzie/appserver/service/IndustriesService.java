package com.kenzie.appserver.service;


import com.kenzie.appserver.controller.model.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IndustriesService {

    private IndustryRepository industryRepository;

    public IndustriesService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<IndustryResponse> findAllIndustries() {

        List<IndustriesRecord> recordList = StreamSupport.stream(industryRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(record -> industryResponseFromRecord(record))
                .collect(Collectors.toList());
    }

    public IndustryResponse findIndustryById(String id) {

        return industryRepository
                .findById(id)
                .map(this::industryResponseFromRecord)
                .orElse(null);
    }

    public List<IndustryResponse> findIndustryByName(String industryName) {
        List<IndustriesRecord> recordList = industryRepository.findByIndustryName(industryName);

        return recordList.stream()
                .map(this::industryResponseFromRecord)
                .collect(Collectors.toList());
    }


    // PRIVATE HELPER METHODS
    private IndustriesRecord industriesRecordFromRequest(IndustryRequest request) {

        IndustriesRecord industriesRecord = new IndustriesRecord();

        industriesRecord.setIndustryId(UUID.randomUUID().toString());
        industriesRecord.setIndustryName(request.getIndustryName());
        industriesRecord.setIndustryDescription(request.getIndustryDescription());


        return industriesRecord;
    }

    private IndustryResponse industryResponseFromRecord(IndustriesRecord industriesRecord) {

        IndustryResponse industryResponse = new IndustryResponse();

        industryResponse.setIndustryId(industriesRecord.getIndustryId());
        industryResponse.setIndustryName(industriesRecord.getIndustryName());
        industryResponse.setIndustryDescription(industriesRecord.getIndustryDescription());

        return industryResponse;
    }
}
