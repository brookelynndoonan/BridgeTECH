package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryResponse;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import com.kenzie.appserver.service.IndustriesService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.Functions.get;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class IndustryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustriesService industriesService;
    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void addNewIndustry_isValid_newIndustryAdded() throws Exception {


        IndustryRequest request = new IndustryRequest();
        request.setIndustryId(UUID.randomUUID().toString());
        request.setIndustryName("Industry Name");
        request.setIndustryDescription("Industry Description");

        // Perform the POST request
        mockMvc.perform(post("/Industry")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())  // Expect HTTP 201 Created status
                .andExpect(jsonPath("Id").exists())  // Check that Id exists but don't check its value
                .andExpect(jsonPath("IndustryName").value(request.getIndustryName()))
                .andExpect(jsonPath("Description").value(request.getIndustryDescription()));
    }

    @Test
    public void updateIndustry_isSuccessful_returnsUpdatedIndustry() throws Exception {

        String Id = "8675309";
        String originalName = "Music";
        String updatedName = "Machine Learning";
        String industryDescription = "Recording Music";

        IndustryRequest originalIndustry = new IndustryRequest();
        originalIndustry.setIndustryId(Id);
        originalIndustry.setIndustryName(originalName);
        originalIndustry.setIndustryDescription(industryDescription);

        industriesService.addNewIndustry(originalIndustry);

        IndustryRequest updatedIndustryRequest = new IndustryRequest();
        updatedIndustryRequest.setIndustryId(Id);
        updatedIndustryRequest.setIndustryName(updatedName);
        updatedIndustryRequest.setIndustryDescription("AI makes your music now...");

        // WHEN
        mockMvc.perform(put("/Industry")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedIndustryRequest)))
                // THEN
                .andExpect(jsonPath("Id")
                        .exists())
                .andExpect(jsonPath("IndustryName")
                        .value(updatedName))
                .andExpect(jsonPath("Description")
                        .value("AI makes your music now..."))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void searchIndustryById_isValid_returnsIndustry() throws Exception {
        String Id = UUID.randomUUID().toString();

        IndustryRequest industryRequest = new IndustryRequest();
        industryRequest.setIndustryName("name");
        industryRequest.setIndustryDescription("Description");
        industryRequest.setIndustryId(Id);

        IndustryResponse industry = industriesService.addNewIndustry(industryRequest);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/Industry/{Id}", industry.getIndustryId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("Id")
                        .value(is(industry.getIndustryId())))
                .andExpect(jsonPath("IndustryName")
                        .value(is(industryRequest.getIndustryName())))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteIndustry_DeleteSuccessful() throws Exception {
        // GIVEN
        String Id = UUID.randomUUID().toString();

        IndustryRequest careerRequest = new IndustryRequest();
        careerRequest.setIndustryName("name");
        careerRequest.setIndustryDescription("Description");
        careerRequest.setIndustryId(Id);

        IndustryResponse industryResponse = industriesService.addNewIndustry(careerRequest);

        // WHEN
        mockMvc.perform(delete("/Industry/{Id}", industryResponse.getIndustryId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
    }

}
