package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.repositories.CareerRepository;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserAccounts;
import com.kenzie.capstone.service.model.UserAccountsRequest;
import com.kenzie.capstone.service.model.UserAccountsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CareerService {
    private CareerRepository careerRepository;
    private LambdaServiceClient lambdaServiceClient;

    public CareerService(CareerRepository careerRepository, LambdaServiceClient lambdaServiceClient) {
        this.careerRepository = careerRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public List<CareerResponse> findAllCareers() {

        List<CareerRecord> recordList = StreamSupport.stream(careerRepository.findAll().spliterator(),
                true).collect(Collectors.toList());

        return recordList.stream()
                .map(this::createCareerResponseFromRecord)
                .collect(Collectors.toList());
    }

    public CareerResponse findCareerById(String id) {

        CareerResponse careerInfo = careerRepository
                .findById(id)
                .map(this::createCareerResponseFromRecord)
                .orElse(null);

        return careerInfo;
    }

    public CareerResponse addNewCareer(CareerCreateRequest careerCreateRequest) {

        CareerRecord careerRecord = createCareerRecordFromRequest(careerCreateRequest);

        careerRepository.save(careerRecord);

        return createCareerResponseFromRecord(careerRecord);


    }

    public CareerResponse updateCareer(String id, String name, String location, String jobDescription, String companyDescription) {

        Optional<CareerRecord> careerExist = careerRepository.findById(id);

        if (careerExist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career Not Found");
        }

        CareerRecord record = careerExist.get();

        record.setCareerName(name);
        record.setLocation(location);
        record.setJobDescription(jobDescription);
        record.setCompanyDescription(companyDescription);

        careerRepository.save(record);

        return createCareerResponseFromRecord(record);

    }


    public void deleteCareer(String id, String userId) {
        Optional<CareerRecord> career = careerRepository.findById(id);

        if (career.isPresent()) {
            CareerRecord careerRecord = career.get();

            if (careerRecord.getId().equals(userId)) {
                careerRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "You are not authorized to delete this career");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Career not found");
        }
    }

    public CareerResponse getUsers(String userId) {
        UserAccounts users = lambdaServiceClient.getUserAccounts(userId);

        if (users != null) {
            CareerResponse careerResponse = new CareerResponse();
            careerResponse.setUserId(users.getId());
            careerResponse.setUserName(users.getName());

            return careerResponse;
        } else {
            return null;
        }
    }

    public CareerResponse createUser(String name, String accountType, String password) throws Exception {
        String userId = UUID.randomUUID().toString();

        UserAccountsRequest userRequest = new UserAccountsRequest(name, accountType, password, userId);

        UserAccountsResponse newUser = lambdaServiceClient.setUserAccounts(userRequest);

        if (newUser != null) {
            CareerResponse careerResponse = new CareerResponse();
            careerResponse.setUserId(newUser.getId());
            careerResponse.setUserName(newUser.getName());
            careerResponse.setAccountType(newUser.getAccountType());
            careerResponse.setPassword(newUser.getPassword());
            return careerResponse;
        } else {
            throw new Exception("Cannot create account");
        }
    }


    // PRIVATE HELPER METHODS
    private CareerRecord createCareerRecordFromRequest(CareerCreateRequest request) {

        CareerRecord record = new CareerRecord();

        record.setId(UUID.randomUUID().toString());
        record.setCareerName(request.getName());
        record.setLocation(request.getLocation());
        record.setJobDescription(request.getJobDescription());
        record.setCompanyDescription(request.getCompanyDescription());

        return record;
    }

    private CareerResponse createCareerResponseFromRecord(CareerRecord careerRecord) {

        CareerResponse response = new CareerResponse();

        response.setId(careerRecord.getId());
        response.setName(careerRecord.getCareerName());
        response.setLocation(careerRecord.getLocation());
        response.setJobDescription(careerRecord.getJobDescription());
        response.setCompanyDescription(careerRecord.getCompanyDescription());

        return response;
    }


}
