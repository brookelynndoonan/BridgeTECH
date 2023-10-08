package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CareerServiceTest {
    private CareerRepository careerRepository;
    private CareerService careerService;
    private LambdaServiceClient lambdaServiceClient;
    private UserAccountRepository userAccountRepository;
    private CacheStore cacheStore;

    public CareerServiceTest(UserAccountRepository userAccountRepository, CacheStore cacheStore) {
        this.userAccountRepository = userAccountRepository;
        this.cacheStore = cacheStore;
    }

    @BeforeEach
    void setup() {
        careerRepository = mock(CareerRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        cacheStore = mock((CacheStore.class));
        careerService = new CareerService(careerRepository, userAccountRepository, lambdaServiceClient,cacheStore);
    }

  /*  @Test
    void findCareerById_isValid_returnsCareer() {
        // GIVEN
        String id = randomUUID().toString();

        CareerRecord record = new CareerRecord();
        record.setId(id);
        record.setCareerName("Career Name");

        // WHEN
        when(careerRepository.findById(id)).thenReturn(Optional.of(record));
        CareerResponse career = careerService.findCareerById(id);

        // THEN
        Assertions.assertNotNull(career, "The object is returned");
        assertEquals(record.getId(), career.getId(), "The id matches");
        assertEquals(record.getCareerName(), career.getName(), "The name matches");
    }*/

   /* @Test
    void findByCareerId_isInvalid_assertsNull() {
        // GIVEN
        String id = randomUUID().toString();

        when(careerRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        CareerResponse career = careerService.findCareerById(id);

        // THEN
        Assertions.assertNull(career, "The career is null when not found");
    }*/

 /*   @Test
    void findAllCareers_isValid_returnsListOfCareers() {
        // GIVEN
        CareerRecord record1 = new CareerRecord();
        record1.setId(randomUUID().toString());
        record1.setCareerName("careerName1");

        CareerRecord record2 = new CareerRecord();
        record2.setId(randomUUID().toString());
        record2.setCareerName("careerName2");

        List<CareerRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(careerRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<CareerResponse> customers = careerService.findAllCareers();

        // THEN
        Assertions.assertNotNull(customers, "The career list is returned");
        assertEquals(2, customers.size(), "There are two careers");

        for (CareerResponse customer : customers) {
            if (customer.getId().equals(record1.getId())) {
                assertEquals(record1.getId(), customer.getId(), "The career id matches");
                assertEquals(record1.getCareerName(), customer.getName(), "The career name matches");
            } else if (customer.getId().equals(record2.getId())) {
                assertEquals(record2.getId(), customer.getId(), "The career id matches");
                assertEquals(record2.getCareerName(), customer.getName(), "The career name matches");
            } else {
                Assertions.fail("Career returned that was not in the records!");
            }
        }
    }*/

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

 /*   @Test
    void updateCareer_isValid_careerIsSuccessfullyUpdated() {
        // GIVEN
        String customerId = randomUUID().toString();

        CareerRecord oldCareerRecord = new CareerRecord();
        oldCareerRecord.setId(customerId);
        oldCareerRecord.setCareerName("oldCareerName");
        oldCareerRecord.setLocation("Asgard");
        oldCareerRecord.setCompanyDescription("Gatekeep of the Bifröst between Earth and realm of the Gods");
        oldCareerRecord.setJobDescription("Protect Heimdall who is the gatekeeper of the Bifröst.");

        String newCareerName = "newName";
        String newLocation = "newLocation";
        String newCompanyDescription = "newCompanyDescription";
        String newJobDescription = "newJobDescription";

        when(careerRepository.findById(customerId)).thenReturn(Optional.of(oldCareerRecord));

        ArgumentCaptor<CareerRecord> careerRecordCaptor = ArgumentCaptor.forClass(CareerRecord.class);

        // WHEN
        careerService.updateCareer(customerId, newCareerName, newLocation, newJobDescription, newCompanyDescription);

        // THEN
        verify(careerRepository).save(careerRecordCaptor.capture());

        CareerRecord record = careerRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The career record is returned");
        assertEquals(record.getId(), customerId, "The career id matches");
        assertEquals(record.getCareerName(), newCareerName, "The career name matches");
        assertEquals(record.getLocation(), newLocation, "The career location matches");
        assertEquals(record.getCompanyDescription(), newCompanyDescription,
                "The company description matches");
        assertEquals(record.getJobDescription(), newJobDescription, "The job description matches");
    }*/

/*    @Test
    void updateCareer_does_not_exist() {
        // GIVEN
        String careerId = randomUUID().toString();

        when(careerRepository.findById(careerId)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(ResponseStatusException.class, () -> careerService.updateCareer(careerId,
                "newName", "newLocation", "newJobDescription",
                "newCompanyDescription"));

        // THEN
        try {
            verify(careerRepository, never()).save(Matchers.any());
        } catch (MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the career" +
                    " is not found in the database. - " + error);
        }

    }*/

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

/*    @Test
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




