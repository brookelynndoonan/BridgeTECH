package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.service.CareerService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@IntegrationTest
public class CareerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CareerService careerService;
    @MockBean
    private CareerRepository careerRepository;

    @Autowired
    CacheStore cache;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void addNewCareer_isValid_newCareerAdded() throws Exception {

        String Id = UUID.randomUUID().toString();
        CareerCreateRequest request = new CareerCreateRequest();
        request.setId(Id);
        request.setName("Smithing");
        request.setJobDescription("Refining Ores.");
        request.setCompanyDescription("We turn raw metal, into beautiful treasures.");
        request.setLocation("Carnival");

        String requestJson = objectMapper.writeValueAsString(request);

        careerService.addNewCareer(request);

        mockMvc.perform(post("/Career")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Id").exists())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.jobDescription").value(request.getJobDescription()))
                .andExpect(jsonPath("$.companyDescription").value(request.getCompanyDescription()))
                .andExpect(jsonPath("$.location").value(request.getLocation()))
                .andDo(print());

    }

    @Test
    public void updateCareer_isSuccessful_returnsUpdatedCareer() throws Exception {

        String Id = "439879";
        String originalName = "Original Name";
        String updatedName = "Updated Name";
        String location = "forest";
        String jobDescription = "Woodcutting";
        String companyDescription = "We cut down trees.";

        CareerCreateRequest originalCareer = new CareerCreateRequest();
        originalCareer.setId(Id);
        originalCareer.setName(originalName);
        originalCareer.setLocation(location);
        originalCareer.setJobDescription(jobDescription);
        originalCareer.setCompanyDescription(companyDescription);

        careerService.addNewCareer(originalCareer);

        CareerCreateRequest updatedCareerRequest = new CareerCreateRequest();
        updatedCareerRequest.setId(Id);
        updatedCareerRequest.setName(updatedName);
        updatedCareerRequest.setLocation("Glacier");
        updatedCareerRequest.setJobDescription("We collect water now.");
        updatedCareerRequest.setCompanyDescription("Drink up.");

        // WHEN
        mockMvc.perform(put("/Career")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedCareerRequest)))
                // THEN
                .andExpect(jsonPath("$.Id").exists())  // Expect the "id" to remain the same
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.location").value("Glacier"))
                .andExpect(jsonPath("$.jobDescription").value("We collect water now."))
                .andExpect(jsonPath("$.companyDescription").value("Drink up."))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void searchCareerById_isValid_returnsCareer() throws Exception {
        CareerCreateRequest careerRequest = new CareerCreateRequest();
        careerRequest.setName(mockNeat.names().get());
        careerRequest.setLocation("location");
        careerRequest.setJobDescription("Job Description");
        careerRequest.setCompanyDescription("Company Description");

        CareerResponse careerResponse = careerService.addNewCareer(careerRequest);

        // WHEN
        ResultActions actions = mockMvc.perform(get("/Career/{Id}", careerResponse.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        CareerResponse response = mapper.readValue(responseBody, CareerResponse.class);

        assertThat(response.getId()).isNotEmpty().as("The ID is populated");
        assertThat(response.getName()).isNotEmpty().as("The name is populated");
        assertThat(response.getLocation()).isNotEmpty().as("The location Exists");
        assertThat(response.getJobDescription()).as("Job Description populated.");
        assertThat(response.getCompanyDescription()).as("Company Description Populated.");
    }

    @Test
    public void testSearchCareerById_CareerNotFound() throws Exception {

        String nonExistentCareerId = UUID.randomUUID().toString();

        careerService.findCareerById(null);

        mockMvc.perform(get("/Career/{Id}", nonExistentCareerId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getUserAccounts_isValid_byId() throws Exception {
        // GIVEN
        String Id = UUID.randomUUID().toString();
        String email = "i_need_more_energy@redbulls.com";

        UserAccountInCareerResponse getuserAccount = careerService.getUsers(Id);

        // WHEN
        mockMvc.perform(get("/Career/userAccounts/user/{Id}",
                        getuserAccount.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("userId")
                        .value(is(Id)))
                .andExpect(jsonPath("email")
                        .value(is(email)))
                .andExpect(status().isOk());
    }


    @Test
    public void createUser_isValid_successful() throws Exception {

        UserAccountInCareerRequest request = new UserAccountInCareerRequest();
        request.setUserName("Bruce Banner");
        request.setPassword("Scienc3Rul3$");
        request.setAccountType("Hulked");
        request.setUserId("usldkfj24");
        request.setEmail("b.banner@StarkIndustries.com");

        String requestJson = objectMapper.writeValueAsString(request);

        careerService.createUser(request);

        mockMvc.perform(post("/Career/userAccounts/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value("usldkfj24"))
                .andExpect(jsonPath("$.userName").value(request.getUserName()))
                .andExpect(jsonPath("$.password").value(request.getPassword()))
                .andExpect(jsonPath("$.accountType").value(request.getAccountType()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andDo(print());
    }


    @Test
    public void createUser_notValid_noUserCreated() throws Exception {
        UserAccountInCareerRequest userAccountInCareerRequest = new UserAccountInCareerRequest();
        userAccountInCareerRequest.setUserName("Peter Parker");
        userAccountInCareerRequest.setAccountType("Regular");
        userAccountInCareerRequest.setPassword("M@ryJ@n3");
        userAccountInCareerRequest.setEmail("p.parker@starkindustries.com");

        when(careerService.createUser(userAccountInCareerRequest))
                .thenThrow(new Exception("User account was not created."));

        mockMvc.perform(post("/Career/userAccounts/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountInCareerRequest)))
                .andExpect(status().isInternalServerError());
    }
}


