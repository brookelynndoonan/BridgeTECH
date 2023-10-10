package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyRequest;
import com.kenzie.appserver.controller.model.CompanyRequestResponse.CompanyResponse;
import com.kenzie.appserver.service.CompaniesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompaniesService companyService;

    private static final ObjectMapper mapper = new ObjectMapper();

    private void cleanUpCompany(String companyId) {
        try {
            companyService.deleteCompany(companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addNewCompany_isValid_newCompanyAdded() throws Exception {

        String Id = UUID.randomUUID().toString();
        CompanyRequest request = new CompanyRequest();
        request.setCompanyName("Name");
        request.setCompanyId(Id);
        request.setCompanyDescription("Company description");

        mockMvc.perform(post("/Company")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("Id")
                        .exists())
                .andExpect(jsonPath("companyName")
                        .value(request.getCompanyName()))
                .andExpect(jsonPath("description")
                        .value(request.getCompanyDescription()));
        cleanUpCompany(Id);
    }

    @Test
    public void updateCompany_isSuccessful_returnsUpdatedCompany() throws Exception {

        String Id = "439879";
        String originalName = "Original Name";
        String updatedName = "Updated Name";
        String companyDescription = "Company Description";

        CompanyRequest originalCompany = new CompanyRequest();
        originalCompany.setCompanyId(Id);
        originalCompany.setCompanyName(originalName);
        originalCompany.setCompanyDescription(companyDescription);

        companyService.addNewCompany(originalCompany);

        CompanyRequest updatedCompanyRequest = new CompanyRequest();
        updatedCompanyRequest.setCompanyId(Id);
        updatedCompanyRequest.setCompanyName(updatedName);
        updatedCompanyRequest.setCompanyDescription(companyDescription);

        // WHEN
        mockMvc.perform(put("/Company")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedCompanyRequest)))
                // THEN
                .andExpect(jsonPath("Id")
                        .exists())
                .andExpect(jsonPath("companyName")
                        .value(updatedName))
                .andExpect(jsonPath("description")
                        .value("Company Description"))
                .andExpect(status().is2xxSuccessful());
        cleanUpCompany(Id);
    }

    @Test
    public void searchCompanyById_isValid_returnsCompany() throws Exception {
        String Id = UUID.randomUUID().toString();

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setCompanyName("name");
        companyRequest.setCompanyId(Id);
        companyRequest.setCompanyDescription("Company Description");

        CompanyResponse company = companyService.addNewCompany(companyRequest);

        // WHEN
        mockMvc.perform(get("/Company/{Id}", company.getCompanyId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("Id")
                        .value(is(company.getCompanyId())))
                .andExpect(jsonPath("companyName")
                        .value(is(company.getCompanyName())))
                .andExpect(status().isOk());
        cleanUpCompany(Id);
    }

    @Test
    public void deleteCompany_DeleteSuccessful() throws Exception {
        // GIVEN
        String Id = UUID.randomUUID().toString();

        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setCompanyName("name");
        companyRequest.setCompanyId(Id);
        companyRequest.setCompanyDescription("Job Description");

        CompanyResponse company = companyService.addNewCompany(companyRequest);

        // WHEN
        mockMvc.perform(delete("/Company/{Id}", company.getCompanyId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
    }

}
