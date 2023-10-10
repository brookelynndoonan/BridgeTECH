package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.service.IndustriesService;
import com.kenzie.appserver.service.model.Industries;
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

    @PutMapping
    public ResponseEntity<IndustryResponse> updateIndustry(@RequestBody IndustryRequest industryRequest) {
        Industries industries = new Industries(industryRequest.getIndustryName(),
                industryRequest.getIndustryDescription(),
                industryRequest.getIndustryId());
        industriesService.updateIndustry(industries);

        IndustryResponse industryResponse = createIndustryResponse(industries);

        return ResponseEntity.ok(industryResponse);
    }

    @GetMapping
    public ResponseEntity<List<IndustryResponse>> getAllIndustries() {
        List<IndustryResponse> industries = industriesService.findAllIndustries();
        if (industries == null || industries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(industries);
    }

    //Blake helped me write this method.
    @GetMapping("/byName/{name}")
    public ResponseEntity<List<IndustryResponse>> getAllIndustriesByName(@PathVariable String name) {
        List<IndustryResponse> industries = industriesService.findAllIndustriesByName(name);
        if (industries == null || industries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(industries);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<IndustryResponse> searchIndustryById(@PathVariable String Id) {
        Industries industry = industriesService.findByIndustriesId(Id);

        if (industry == null) {
            return ResponseEntity.notFound().build();
        }

        IndustryResponse industryResponse = createIndustryResponse(industry);
        return ResponseEntity.ok(industryResponse);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteIndustryById(@PathVariable("Id") String industryId) {
        industriesService.deleteIndustry(industryId);
        return ResponseEntity.ok().build();
    }

    private IndustryResponse createIndustryResponse(Industries industries) {
        IndustryResponse industryResponse = new IndustryResponse();
        industryResponse.setIndustryId(industries.getIndustryId());
        industryResponse.setIndustryName(industries.getIndustryName());
        industryResponse.setIndustryDescription(industries.getIndustryDescription());
        return industryResponse;
    }
}

