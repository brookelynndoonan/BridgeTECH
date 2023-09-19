package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.service.model.Career;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.stubbing.Answer;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class CareerServiceTest {
    private CareerRepository careerRepository;
    private CareerService careerService;
    //private LambdaServiceClient lambdaServiceClient;

    @BeforeEach
    void setup() {
        careerRepository = mock(CareerRepository.class);
        careerService = new CareerService(careerRepository);
    }

    @Test
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
        Assertions.assertEquals(record.getId(), career.getId(), "The id matches");
        Assertions.assertEquals(record.getCareerName(), career.getName(), "The name matches");
    }

    @Test
    void findByCareerId_isInvalid_assertsNull() {
        // GIVEN
        String id = randomUUID().toString();

        when(careerRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        CareerResponse career = careerService.findCareerById(id);

        // THEN
        Assertions.assertNull(career, "The example is null when not found");
    }

    @Test
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
        Assertions.assertEquals(2, customers.size(), "There are two careers");

        for (CareerResponse customer : customers) {
            if (customer.getId().equals(record1.getId())) {
                Assertions.assertEquals(record1.getId(), customer.getId(), "The career id matches");
                Assertions.assertEquals(record1.getCareerName(), customer.getName(), "The career name matches");
            } else if (customer.getId().equals(record2.getId())) {
                Assertions.assertEquals(record2.getId(), customer.getId(), "The career id matches");
                Assertions.assertEquals(record2.getCareerName(), customer.getName(), "The career name matches");
            } else {
                Assertions.fail("Career returned that was not in the records!");
            }
        }
    }

    @Test
    void addNewCareer_isValid_careerIsAdded() {
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
        Assertions.assertEquals(record.getCareerName(), careerName, "The career name matches");
    }

    @Test
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
        Assertions.assertEquals(record.getId(), customerId, "The career id matches");
        Assertions.assertEquals(record.getCareerName(), newCareerName, "The career name matches");
        Assertions.assertEquals(record.getLocation(), newLocation, "The career location matches");
        Assertions.assertEquals(record.getCompanyDescription(), newCompanyDescription,
                "The company description matches");
        Assertions.assertEquals(record.getJobDescription(), newJobDescription, "The job description matches");
    }

    @Test
    void updateCareer_does_not_exist() {
        // GIVEN
        String careerId = randomUUID().toString();

        when(careerRepository.findById(careerId)).thenReturn(Optional.empty());

        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> careerService.updateCareer(careerId,
                "newName", "newLocation", "newJobDescription",
                "newCompanyDescription"));

        // THEN
        try {
            verify(careerRepository, never()).save(Matchers.any());
        } catch (MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the career" +
                    " is not found in the database. - " + error);
        }

    }

    @Test
    void deleteCareer_isSuccessful() {
        String careerId = randomUUID().toString();

        // WHEN
        careerService.deleteCareer(careerId);

        // THEN
        verify(careerRepository).deleteById(careerId);

    }


}
