package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.config.LambdaServiceClientConfiguration;
import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.controller.model.CreateUserRequest;
import com.kenzie.appserver.service.CareerService;
import com.kenzie.appserver.service.model.Career;
import com.kenzie.appserver.service.model.Example;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
public class CareerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CareerService careerService;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        mapper.registerModule(new Jdk8Module());
    }


    @Test
    public void getById_Exists() throws Exception {
        CareerResponse userId = careerService.getUsers("userId");

        mockMvc.perform(get("/Industry/user/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("userId").isString())
                .andExpect(status().is2xxSuccessful());
    }
}
