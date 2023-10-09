package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStoreCareer;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.service.model.Career;
import com.kenzie.capstone.service.client.LambdaServiceClient;

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
import static org.junit.jupiter.api.Assertions.*;
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
    void findByCareerId_cacheNotNull_returnCachedCareer(){

        String careerId = randomUUID().toString();
        Career career = new Career(careerId, "career name", "location",
                "jobDescription", "companyDescription");

        when(cache.get(careerId)).thenReturn(career);

        Career actualCareer = careerService.findCareerById(careerId);

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

   /* @Test
    void getUsers_isValid_byUserId() {
        // GIVEN
        UserAccounts fakeAccount = new UserAccounts();
        fakeAccount.setId("5464768");
        fakeAccount.setName("Pepper Potts");

        // WHEN
        when(lambdaServiceClient.getUserAccounts("5464768")).thenReturn(fakeAccount);

        // THEN
        UserAccountInCareerResponse userAccountsResponse = careerService.getUsers("5464768");

        assertEquals("5464768", userAccountsResponse.getUserId());
        assertEquals("Pepper Potts", userAccountsResponse.getUserName());
    }*/

    @Test
    public void testGetUsers_NullResponse() {
        // GIVEN/WHEN
        when(lambdaServiceClient.getUserAccounts(anyString())).thenReturn(null);

        UserAccountInCareerResponse userAccountsResponse = careerService.getUsers("452452");

        // THEN
        assertNull(userAccountsResponse);
    }

   /* @Test
    public void testCreateUser_Success() throws Exception {
        // GIVEN
        UserAccountsResponse userAccountsResponse = new UserAccountsResponse();
        userAccountsResponse.setId("74654");
        userAccountsResponse.setUserName("Remy LeBeau");
        userAccountsResponse.setAccountType("Card Magic");
        userAccountsResponse.setPassword("G@mb1t");

        // WHEN
        when(lambdaServiceClient.setUserAccounts(any(UserAccountsRequest.class))).thenReturn(userAccountsResponse);

        UserAccountInCareerResponse userAccountInCareerResponse = careerService.createUser("Remy LeBeau", "Card Magic",
                "G@mb1t", "561");

        // THEN
        assertEquals("74654", userAccountsResponse.getId());
        assertEquals("Remy LeBeau", userAccountsResponse.getUserName());
        assertEquals("Card Magic", userAccountsResponse.getAccountType());
        assertEquals("G@mb1t", userAccountsResponse.getPassword());
    }*/

 /*   @Test
    public void testCreateUser_NullResponse() throws Exception {
        // GIVEN/WHEN
        when(lambdaServiceClient.setUserAccounts(any(UserAccountsRequest.class))).thenReturn(null);

        // THEN
        assertThrows(Exception.class, () -> {
            careerService.createUser("Dr. Jean Grey", "Professor", "Ph0en1xRul3z", "3615");
        });
    }*/

}




