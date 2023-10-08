package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IndustriesService {

    private IndustryRepository industryRepository;

    public IndustriesService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<IndustryResponse> findAllIndustries() {

        List<IndustriesRecord> recordList = StreamSupport.stream(industryRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(this::industryResponseFromRecord)
                .collect(Collectors.toList());
    }

    public IndustryResponse findIndustryById(String id) {

        return industryRepository
                .findById(id)
                .map(this::industryResponseFromRecord)
                .orElse(null);
    }

    public IndustriesRecord findIndustryByName(String name) {

        if (name != null) {

            return industryRepository.findIndustryByName(name);
        } else {
            return null;
        }
    }

    public List<IndustryResponse> findAllIndustriesByName(String industryName) {
        List<IndustriesRecord> recordList = industryRepository.findByIndustryName(industryName);

        return recordList.stream()
                .map(this::industryResponseFromRecord)
                .collect(Collectors.toList());
    }

    public IndustryResponse addNewIndustry(IndustryRequest industryRequest) {

        IndustriesRecord industryRecord = industriesRecordFromRequest(industryRequest);

        industryRepository.save(industryRecord);

        return industryResponseFromRecord(industryRecord);


    }

    public IndustryResponse updateIndustry(String id, String name, String description) {

        Optional<IndustriesRecord> industryExist = industryRepository.findById(id);

        if (industryExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Industry Not Found");
        }

        IndustriesRecord record = industryExist.get();

        record.setIndustryName(name);
        record.setIndustryDescription(description);

        industryRepository.save(record);

        return industryResponseFromRecord(record);

    }


    public void deleteIndustry(String id) {
        industryRepository.deleteById(id);
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
