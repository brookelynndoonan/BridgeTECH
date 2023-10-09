package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.IndustryRequestResponse.IndustryRequest;
import com.kenzie.appserver.repositories.IndustryRepository;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import com.kenzie.appserver.service.IndustriesService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.Functions.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
public class IndustrControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustriesService industriesService;
    @MockBean
    private IndustryRepository industryRepository;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void addNewIndustry_isValid_newIndustryAdded() throws Exception {

        String industryId = UUID.randomUUID().toString();

        IndustryRequest request = new IndustryRequest();
        request.setIndustryId(industryId);
        request.setIndustryName("Industry Name");
        request.setIndustryDescription("Industry Description");

        String requestJson = objectMapper.writeValueAsString(request);

       industriesService.addNewIndustry(request);

        mockMvc.perform(post("/Industry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.industryId").value(industryId))
                .andExpect(jsonPath("$.industryName").value(request.getIndustryName()))
                .andExpect(jsonPath("$.industryDescription").value(request.getIndustryDescription()));
    }

/*    @Test
    public void getAllIndustries_isSuccessful() throws Exception {

        IndustriesRecord industries1 = new IndustriesRecord();
        industries1.setIndustryName("IndustryName1");
        industries1.setIndustryDescription("IndustryDescription1");
        industries1.setIndustryId(UUID.randomUUID().toString());

        IndustriesRecord industries2 = new IndustriesRecord();
        industries2.setIndustryName("IndustryName2");
        industries2.setIndustryDescription("IndustryDescription2");
        industries2.setIndustryId(UUID.randomUUID().toString());

        List<IndustriesRecord> records = Arrays.asList(industries1, industries2);
        industryRepository.findAll();
        industriesService.findAllIndustries();

        mockMvc.perform(get("/Industries",records)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

}
