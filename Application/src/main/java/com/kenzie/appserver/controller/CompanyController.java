package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyResponse;
import com.kenzie.appserver.service.CompaniesService;
import com.kenzie.appserver.service.model.Companies;
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

    @PutMapping
    public ResponseEntity<CompanyResponse> updateCompany(@RequestBody CompanyRequest companyRequest) {
        Companies companies = new Companies(companyRequest.getCompanyName(),
                companyRequest.getCompanyDescription(),
                companyRequest.getCompanyId());
        companiesService.updateCompany(companies);

        CompanyResponse companyResponse = createCompanyResponse(companies);

        return ResponseEntity.ok(companyResponse);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> companies = companiesService.findAllCompanies();
        if (companies == null || companies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(companies);
    }

    //Blake helped me write this method.
    @GetMapping("/byName/{name}")
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesByName(@PathVariable String name) {
        List<CompanyResponse> companies = companiesService.findAllCompaniesByName(name);
        if (companies == null || companies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<CompanyResponse> searchCompanyById(@PathVariable("Id") String companyId) {
        Companies companies = companiesService.findByCompaniesId(companyId);

        if (companies == null) {
            return ResponseEntity.notFound().build();
        }

        CompanyResponse companyResponse = createCompanyResponse(companies);
        return ResponseEntity.ok(companyResponse);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteCompanyById(@PathVariable("Id") String companyId) {
        companiesService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }

    private CompanyResponse createCompanyResponse(Companies company) {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setCompanyName(company.getCompanyName());
        companyResponse.setCompanyId(company.getCompanyId());
        companyResponse.setCompanyDescription(company.getCompanyDescription());
        return companyResponse;
    }
}

