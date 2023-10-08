package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyResponse;
import com.kenzie.appserver.repositories.model.CompanyRecord;
import com.kenzie.appserver.service.CompaniesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/Company")
public class CompanyController {

    private final CompaniesService companiesService;

    CompanyController(CompaniesService companiesService) {
        this.companiesService = companiesService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> addNewCompany(@RequestBody CompanyRequest companyRequest) {

        if (companyRequest.getCompanyName() == null || companyRequest.getCompanyName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Company Name");
        }

        CompanyResponse response = companiesService.addNewCompany(companyRequest);

        return ResponseEntity.created(URI.create("/companies/" + response.getCompanyId())).body(response);
    }

    @PostMapping("/{Id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable("Id")
                                                         @RequestBody CompanyRequest companyRequest) {
        CompanyResponse response = companiesService.updateCompany(companyRequest.getCompanyName(),
                companyRequest.getCompanyId(), companyRequest.getCompanyDescription());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> companies = companiesService.findAllCompanies();
        if (companies == null || companies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesByName(@RequestParam("companyName") String companyName) {
        List<CompanyResponse> companies = companiesService.findAllCompaniesByName(companyName);
        if (companies == null || companies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/company/{Id}")
    public ResponseEntity<CompanyResponse> searchCompanyById(@PathVariable("Id") String companyId) {
        CompanyResponse companyResponse = companiesService.findCompanyById(companyId);
        if (companyResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyResponse);
    }

    @GetMapping("/companyName/{CompanyName}")
    public ResponseEntity<CompanyRecord> searchCompanyByName(@PathVariable("CompanyName") String companyName) {
        CompanyRecord companyRecord = companiesService.findCompanyByName(companyName);
        if (companyRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyRecord);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteCompanyById(@PathVariable("Id") String companyId) {
        companiesService.deleteCompany(companyId);
        return ResponseEntity.ok().build();
    }
}

