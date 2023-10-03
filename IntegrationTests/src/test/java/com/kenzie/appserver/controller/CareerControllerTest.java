package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.service.CareerService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
public class CareerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CareerService careerService;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        mapper.registerModule(new Jdk8Module());
    }


    @Test
    public void getById_Exists() throws Exception {
        CareerResponse expectedResponse = new CareerResponse();
        expectedResponse.setUserId("65412");

        when(careerService.getUsers(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(get("/Career/user/{id}", "65412")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("userId").value("65412"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        CareerCreateRequest request = new CareerCreateRequest();
        request.setName("John Doe");
        request.setAccountType("Standard");
        request.setPassword("password123");

        CareerResponse expectedResponse = new CareerResponse();
        expectedResponse.setUserId("6541");
        expectedResponse.setUserName("Bruce Banner");
        expectedResponse.setAccountType("Hulked");
        expectedResponse.setPassword("Scienc3Rul3$");

        when(careerService.createUser(any(String.class), any(String.class), any(String.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/Career/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/Career/user/6541"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Bruce Banner"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("Hulked"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Scienc3Rul3$"));
    }

    @Test
    public void createUser_notValid_noUserCreated() throws Exception {
        CareerCreateRequest request = new CareerCreateRequest();
        request.setName("Peter Parker");
        request.setAccountType("Regular");
        request.setPassword("M@ryJ@n3");

        when(careerService.createUser(any(String.class), any(String.class), any(String.class)))
                .thenThrow(new Exception("User account was not created."));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/Career/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}


