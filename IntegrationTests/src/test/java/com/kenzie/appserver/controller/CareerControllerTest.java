package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerRequest;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.service.CareerService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
public class CareerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CareerService careerService;
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

        mockMvc.perform(post("/Career")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("Id")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(request.getName()))
                .andExpect(jsonPath("jobDescription")
                        .value(request.getJobDescription()))
                .andExpect(jsonPath("companyDescription")
                        .value(request.getCompanyDescription()))
                .andExpect(jsonPath("location")
                        .value(request.getLocation()));
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
                .andExpect(jsonPath("$.Id")
                        .exists())
                .andExpect(jsonPath("$.name")
                        .value(updatedName))
                .andExpect(jsonPath("$.location")
                        .value("Glacier"))
                .andExpect(jsonPath("$.jobDescription")
                        .value("We collect water now."))
                .andExpect(jsonPath("$.companyDescription")
                        .value("Drink up."))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void searchCareerById_isValid_returnsCareer() throws Exception {
        String Id = UUID.randomUUID().toString();

        CareerCreateRequest careerRequest = new CareerCreateRequest();
        careerRequest.setName("name");
        careerRequest.setLocation("location");
        careerRequest.setJobDescription("Job Description");
        careerRequest.setCompanyDescription("Company Description");
        careerRequest.setId(Id);

        CareerResponse career = careerService.addNewCareer(careerRequest);

        // WHEN
        mockMvc.perform(get("/Career/{Id}", career.getId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("Id")
                        .value(is(career.getId())))
                .andExpect(jsonPath("name")
                        .value(is(careerRequest.getName())))
                .andExpect(status().isOk());
    }

    @Test
    public void searchCareerById_CareerNotFound() throws Exception {

        String nonExistentCareerId = UUID.randomUUID().toString();

        careerService.findCareerById("idThatDoesNotExist");

        mockMvc.perform(get("/Career/{Id}", nonExistentCareerId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCareer_DeleteSuccessful() throws Exception {
        // GIVEN
        String Id = UUID.randomUUID().toString();

        CareerCreateRequest careerRequest = new CareerCreateRequest();
        careerRequest.setName("name");
        careerRequest.setLocation("location");
        careerRequest.setJobDescription("Job Description");
        careerRequest.setCompanyDescription("Company Description");
        careerRequest.setId(Id);

        CareerResponse career = careerService.addNewCareer(careerRequest);

        // WHEN
        mockMvc.perform(delete("/Career/{Id}", career.getId())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUserAccounts_isValid_byEmail() throws Exception {
        // GIVEN
        String Id = UUID.randomUUID().toString();
        String email = "i_need_more_energy@redbulls.com";

        UserAccountInCareerRequest request = new UserAccountInCareerRequest();
        request.setAccountType("Basic user");
        request.setEmail(email);
        request.setPassword("password");
        request.setUserId(Id);
        request.setUserName("Username");

        careerService.createUser(request);

        // WHEN
        mockMvc.perform(get("/Career/userAccounts/user/{email}", email)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(jsonPath("password")
                        .value(is(request.getPassword()
                        )))
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

        mockMvc.perform(post("/Career/userAccounts/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))

                .andExpect(jsonPath("userId")
                        .value("usldkfj24"))
                .andExpect(jsonPath("userName")
                        .value(request.getUserName()))
                .andExpect(jsonPath("password")
                        .value(request.getPassword()))
                .andExpect(jsonPath("accountType")
                        .value(request.getAccountType()))
                .andExpect(jsonPath("email").value(request.getEmail()));
    }
}


