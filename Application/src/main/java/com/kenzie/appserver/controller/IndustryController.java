package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import com.kenzie.appserver.service.IndustriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/Industry")
public class IndustryController {

    private final IndustriesService industriesService;

    IndustryController(IndustriesService industriesService) {
        this.industriesService = industriesService;
    }

    @PostMapping
    public ResponseEntity<IndustryResponse> addNewIndustry(@RequestBody IndustryRequest industryRequest) {

        if (industryRequest.getIndustryName() == null || industryRequest.getIndustryName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Industry Name");
        }

        IndustryResponse response = industriesService.addNewIndustry(industryRequest);

        return ResponseEntity.created(URI.create("/industries/" + response.getIndustryId())).body(response);
    }

    @PostMapping("/{Id}")
    public ResponseEntity<IndustryResponse> updateIndustry(@PathVariable("Id")
                                                           @RequestBody IndustryRequest industryRequest) {
        IndustryResponse response = industriesService.updateIndustry(industryRequest.getIndustryName(),
                industryRequest.getIndustryId(), industryRequest.getIndustryDescription());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IndustryResponse>> getAllIndustries() {
        List<IndustryResponse> industries = industriesService.findAllIndustries();
        if (industries == null || industries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(industries);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<IndustryResponse>> getAllIndustriesByName(@RequestParam("industryName")String industryName) {
        List<IndustryResponse> industries = industriesService.findAllIndustriesByName(industryName);
        if (industries == null || industries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(industries);
    }

    @GetMapping("/industry/{Id}")
    public ResponseEntity<IndustryResponse> searchIndustryById(@PathVariable("Id") String industryId) {
        IndustryResponse industryResponse = industriesService.findIndustryById(industryId);
        if (industryResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(industryResponse);
    }

    @GetMapping("/industryName/{IndustryName}")
    public ResponseEntity<IndustriesRecord> searchIndustryByName(@PathVariable("IndustryName") String industryName) {
        IndustriesRecord industriesRecord = industriesService.findIndustryByName(industryName);
        if (industriesRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(industriesRecord);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteIndustryById(@PathVariable("Id") String industryId) {
        industriesService.deleteIndustry(industryId);
        return ResponseEntity.ok().build();
    }
}

