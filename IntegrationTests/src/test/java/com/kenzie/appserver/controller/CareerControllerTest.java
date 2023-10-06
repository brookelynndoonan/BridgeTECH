package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;

import com.kenzie.appserver.controller.model.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.service.CareerService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.com.google.common.base.Verify.verify;



@IntegrationTest
public class CareerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CareerService careerService;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        mapper.registerModule(new Jdk8Module());
    }


    @Test
    public void addNewCareer_isValid_newCareerAdded() throws Exception {

        CareerCreateRequest request = new CareerCreateRequest();
        request.setName("Smithing");
        request.setJobDescription("Refining Ores.");
        request.setCompanyDescription("We turn raw metal, into beautiful treasures.");

        String requestJson = objectMapper.writeValueAsString(request);

        CareerResponse mockResponse = new CareerResponse();
        mockResponse.setId("654654");
        mockResponse.setName(request.getName());
        mockResponse.setJobDescription(request.getJobDescription());
        mockResponse.setCompanyDescription(request.getCompanyDescription());

        when(careerService.addNewCareer(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/Career")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Id", is("654654")))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.jobDescription", is(request.getJobDescription())))
                .andExpect(jsonPath("$.companyDescription", is(request.getCompanyDescription())))
                .andDo(print());

    }

    @Test
    public void getUserAccounts_isValid_byId() throws Exception {
        UserAccountInCareerResponse userAccountInCareerResponse = new UserAccountInCareerResponse();
        userAccountInCareerResponse.setUserId("65412");

        when(careerService.getUsers(anyString())).thenReturn(userAccountInCareerResponse);

        mockMvc.perform(get("/Career/user/{Id}", "65412")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("userId").value("65412"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createUser_isValid_successful() throws Exception {
        String userName = "Bruce Banner";
        String password = "Scienc3Rul3$";
        String accountType = "Hulked";
        String userId = "usldkfj24";
        String email = "b.banner@StarkIndustries.com";

        UserAccountInCareerRequest userAccountInCareerRequest = new UserAccountInCareerRequest();
        userAccountInCareerRequest.setUserName(userName);
        userAccountInCareerRequest.setAccountType(accountType);
        userAccountInCareerRequest.setPassword(password);
        userAccountInCareerRequest.setUserId(userId);
        userAccountInCareerRequest.setEmail(email);

        careerService.createUser(userAccountInCareerRequest);


        mapper.registerModule(new JavaTimeModule());

        ResultActions actions = mockMvc.perform(post("/Career/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountInCareerRequest)))
                .andExpect(jsonPath("email")
                        .value(is(email)))
                .andExpect(jsonPath("userName")
                        .value(is(userName)))
                .andExpect(jsonPath("accountType")
                        .value(is(accountType)))
                .andExpect(jsonPath("password")
                        .value(is(password)));

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserAccountInCareerResponse response = mapper.readValue(responseBody, UserAccountInCareerResponse.class);
        assertThat(response.getUserId()).isNotEmpty().as("The ID is populated");
    }


    @Test
    public void createUser_notValid_noUserCreated() throws Exception {
        UserAccountInCareerRequest userAccountInCareerRequest = new UserAccountInCareerRequest();
        userAccountInCareerRequest.setUserName("Peter Parker");
        userAccountInCareerRequest.setAccountType("Regular");
        userAccountInCareerRequest.setPassword("M@ryJ@n3");

        when(careerService.createUser(userAccountInCareerRequest))
                .thenThrow(new Exception("User account was not created."));

        mockMvc.perform(post("/Career/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountInCareerRequest)))
                .andExpect(status().isInternalServerError());
    }
}


