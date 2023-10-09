package com.kenzie.appserver.service;

import com.kenzie.appserver.config.cachestore.CacheStoreCareer;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.service.model.Career;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserAccountRecord;
import com.kenzie.capstone.service.model.UserAccountsRequest;
import com.kenzie.capstone.service.model.UserAccountsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CareerServiceTest {
    private CareerRepository careerRepository;
    private CareerService careerService;
    private LambdaServiceClient lambdaServiceClient;
    private UserAccountRepository userAccountRepository;
    private CacheStoreCareer cache;

    @BeforeEach
    void setup() {
        careerRepository = mock(CareerRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        userAccountRepository = mock(UserAccountRepository.class);
        cache = mock((CacheStoreCareer.class));
        careerService = new CareerService(careerRepository, userAccountRepository, lambdaServiceClient, cache);
    }

    @Test
    void findAllCareers_twoCareers_isValid() {
        // GIVEN
        CareerRecord record1 = new CareerRecord();
        record1.setId(randomUUID().toString());
        record1.setCareerName("career1");
        record1.setLocation("location1");
        record1.setJobDescription("jobDescription1");
        record1.setCompanyDescription("companyDescription1");

        CareerRecord record2 = new CareerRecord();
        record1.setId(randomUUID().toString());
        record1.setCareerName("career2");
        record1.setLocation("location2");
        record1.setJobDescription("jobDescription2");
        record1.setCompanyDescription("companyDescription2");

        List<CareerRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(careerRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<Career> careers = careerService.findAllCareers();

        // THEN
        Assertions.assertNotNull(careers, "The career list is returned");
        Assertions.assertEquals(2, careers.size(), "There are two careers");

        for (Career career : careers) {
            if (Objects.equals(career.getId(), record1.getId())) {
                Assertions.assertEquals(record1.getId(), career.getId(), "The career Id matches");
                Assertions.assertEquals(record1.getCareerName(), career.getCareerName(), "The career name matches");
                Assertions.assertEquals(record1.getLocation(), career.getLocation(), "The career location matches");
                Assertions.assertEquals(record1.getJobDescription(), career.getJobDescription(),
                        "The job description matches");
                Assertions.assertEquals(record1.getCompanyDescription(), career.getCompanyDescription(),
                        "The company description matches");

            } else if (Objects.equals(career.getId(), record2.getId())) {
                Assertions.assertEquals(record2.getId(), career.getId(), "The career Id matches");
                Assertions.assertEquals(record2.getCareerName(), career.getCareerName(), "The career name matches");
                Assertions.assertEquals(record2.getLocation(), career.getLocation(), "The location matches");
                Assertions.assertEquals(record2.getJobDescription(), career.getJobDescription(),
                        "The job description matches");
                Assertions.assertEquals(record2.getCompanyDescription(), career.getCompanyDescription(),
                        "The company description matches");
            } else {
                Assertions.fail("Career returned that was not in the records!");
            }
        }
    }

    @Test
    void findByCareerId() {
        // GIVEN
        String careerId = randomUUID().toString();

        CareerRecord record = new CareerRecord();
        record.setId(careerId);
        record.setCareerName("Career Name");
        record.setLocation("Location");
        record.setJobDescription("Job Description");
        record.setCompanyDescription("Company Description");
        when(careerRepository.findById(careerId)).thenReturn(Optional.of(record));
        // WHEN
        Career career = careerService.findCareerById(careerId);

        // THEN
        Assertions.assertNotNull(career, "The career is returned");
        Assertions.assertEquals(record.getId(), career.getId(), "The career id matches");
        Assertions.assertEquals(record.getCareerName(), career.getCareerName(), "The career name matches");
        Assertions.assertEquals(record.getLocation(), career.getLocation(), "The location matches");
        Assertions.assertEquals(record.getJobDescription(), career.getJobDescription(), "The job description matches");
        Assertions.assertEquals(record.getCompanyDescription(), career.getCompanyDescription(), "The company description matches");
    }

    @Test
    void findByCareerId_isNull_returnsNothing() {
        // GIVEN
        String careerId = randomUUID().toString();

        when(careerRepository.findById(careerId)).thenReturn(Optional.empty());
        // WHEN
        Career career = careerService.findCareerById(careerId);

        // THEN
        Assertions.assertNull(career);
    }

    @Test
    void findByCareerId_cacheNotNull_returnCachedCareer() {
        // GIVEN
        String careerId = randomUUID().toString();
        Career career = new Career(careerId, "career name", "location",
                "jobDescription", "companyDescription");

        // WHEN
        when(cache.get(careerId)).thenReturn(career);

        Career actualCareer = careerService.findCareerById(careerId);

        // THEN
        Assertions.assertEquals(career, actualCareer);
    }

    @Test
    void addNewCareer_isValid_careerIsAdded() {
        // GIVEN
        String careerName = "careerName";

        CareerCreateRequest request = new CareerCreateRequest();
        request.setName(careerName);

        ArgumentCaptor<CareerRecord> careerRecordCaptor = ArgumentCaptor.forClass(CareerRecord.class);

        // WHEN
        CareerResponse returnedCareer = careerService.addNewCareer(request);

        // THEN
        Assertions.assertNotNull(returnedCareer);

        verify(careerRepository).save(careerRecordCaptor.capture());

        CareerRecord record = careerRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The career record is returned");
        Assertions.assertNotNull(record.getId(), "The career id exists");
        assertEquals(record.getCareerName(), careerName, "The career name matches");
    }

    @Test
    void updateCareer_isValid_careerIsSuccessfullyUpdated() {
        // GIVEN
        String Id = randomUUID().toString();

        Career career = new Career(Id, "careerName", "location",
                "jobDescription", "companyDescription");

        ArgumentCaptor<CareerRecord> careerRecordCaptor = ArgumentCaptor
                .forClass(CareerRecord.class);

        // WHEN
        when(careerRepository.existsById(Id)).thenReturn(true);
        careerService.updateCareer(career);

        // THEN
        verify(careerRepository).existsById(Id);
        verify(careerRepository).save(careerRecordCaptor.capture());
        CareerRecord careerRecord = careerRecordCaptor.getValue();

        Assertions.assertEquals(career.getId(), careerRecord.getId());
        Assertions.assertEquals(career.getCareerName(), careerRecord.getCareerName());
        Assertions.assertEquals(career.getLocation(), careerRecord.getLocation());
        Assertions.assertEquals(career.getCompanyDescription(), careerRecord.getCompanyDescription());
        Assertions.assertEquals(career.getJobDescription(), careerRecord.getJobDescription());
    }

    @Test
    void updateCareerById_ifIdNull_noExistingIdReturnNull() {
        String careerId = randomUUID().toString();

        Career career = new Career(careerId, "careerName", "location", "jobDescription",
                "companyDescription");

        // WHEN
        when(careerRepository.existsById(careerId)).thenReturn(false);
        careerService.updateCareer(career);


        // THEN
        verify(careerRepository).existsById(careerId);
        verify(careerRepository, times(0)).save(any());

    }

    @Test
    void deleteCareer_idMatches_isSuccessful() {
        // GIVEN
        String careerId = randomUUID().toString();
        String userId = "Black Widow";

        CareerRecord careerRecord = new CareerRecord();
        careerRecord.setId(careerId);
        careerRecord.setId(userId);

        // WHEN
        when(careerRepository.findById(careerId)).thenReturn(Optional.of(careerRecord));

        careerService.deleteCareer(careerId, userId);

        // THEN
        verify(careerRepository).deleteById(careerId);
    }

    @Test
    void deleteCareer_unauthorized_notSuccessful() {
        // GIVEN
        String careerId = randomUUID().toString();
        String userId = "Thor";
        String unauthorizedUserId = "Star Lord";

        CareerRecord careerRecord = new CareerRecord();
        careerRecord.setId(careerId);
        careerRecord.setId(userId);

        // WHEN
        when(careerRepository.findById(careerId)).thenReturn(Optional.of(careerRecord));

        // THEN
        assertThrows(ResponseStatusException.class, () -> {
            careerService.deleteCareer(careerId, unauthorizedUserId);
        });

        verify(careerRepository, never()).deleteById(careerId);
    }

    @Test
    void deleteCareer_careerNotFound_notSuccessful() {
        // GIVEN
        String careerId = randomUUID().toString();
        String userId = "Nicholas Fury";

        // WHEN
        when(careerRepository.findById(careerId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResponseStatusException.class, () -> {
            careerService.deleteCareer(careerId, userId);
        });

        verify(careerRepository, never()).deleteById(careerId);
    }

    @Test
    public void getUsersWithExistingEmail_isValid_returnsUser() {
        // GIVEN
        String email = "knights.of.falador@gielinorguards.com";
        UserAccountRecord userAccountRecord = new UserAccountRecord();
        userAccountRecord.setEmail(email);
        userAccountRecord.setPassword("password");
        when(userAccountRepository.findById(email)).thenReturn(Optional.of(userAccountRecord));

        // WHEN
        UserAccountInCareerResponse response = careerService.getUsers(email);

        // THEN
        assertEquals(email, response.getEmail());
        assertEquals("password", response.getPassword());
    }

    @Test
    public void getUsersWithNonExistingEmail_isNotValid_noUser() {
        // GIVEN
        String email = "noemail@doesntexistland.com";
        when(userAccountRepository.findById(email)).thenReturn(Optional.empty());

        // WHEN
        UserAccountInCareerResponse response = careerService.getUsers(email);

        // THEN
        assertEquals(null, response);
    }

    @Test
    public void CreateUser_isValid_userCreated() {
        // GIVEN
        UserAccountsRequest userAccountsRequest = new UserAccountsRequest();
        userAccountsRequest.setUserId("74654");
        userAccountsRequest.setUserName("Remy LeBeau");
        userAccountsRequest.setAccountType("Card Magic");
        userAccountsRequest.setPassword("G@mb1t");

        UserAccountInCareerRequest userAccountsCareerRequest = new UserAccountInCareerRequest();
        userAccountsCareerRequest.setUserId("74654");
        userAccountsCareerRequest.setUserName("Remy LeBeau");
        userAccountsCareerRequest.setAccountType("Card Magic");
        userAccountsCareerRequest.setPassword("G@mb1t");

        UserAccountsResponse userAccountsResponse = new UserAccountsResponse();
        userAccountsResponse.setId(userAccountsRequest.getUserId());
        userAccountsResponse.setUserName(userAccountsRequest.getUserName());
        userAccountsResponse.setAccountType(userAccountsRequest.getAccountType());
        userAccountsResponse.setPassword(userAccountsRequest.getPassword());

        when(lambdaServiceClient.setUserAccounts(userAccountsRequest)).thenReturn(userAccountsResponse);

        // WHEN
        UserAccountInCareerResponse response = careerService.createUser(userAccountsCareerRequest);

        // THEN
        assertEquals("74654", response.getUserId());
        assertEquals("Remy LeBeau", response.getUserName());
        assertEquals("Card Magic", response.getAccountType());
        assertEquals("G@mb1t", response.getPassword());
    }


}




