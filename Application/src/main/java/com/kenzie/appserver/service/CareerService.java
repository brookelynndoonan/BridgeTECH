package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.repositories.CareerRepository;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserAccountRecord;
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
    private UserAccountRepository userAccountRepository;
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

    public UserAccountInCareerResponse getUsers(String userId) {
        UserAccounts users = lambdaServiceClient.getUserAccounts(userId);

        if (users != null) {
            UserAccountInCareerResponse userAccountInCareerResponse = new UserAccountInCareerResponse();
            userAccountInCareerResponse.setUserId(users.getId());
            userAccountInCareerResponse.setUserName(users.getName());

            return userAccountInCareerResponse;
        } else {
            return null;
        }
    }

    public UserAccountInCareerResponse createUser(UserAccountInCareerRequest createUserRequest) {

        UserAccountRecord userAccountRecord = new UserAccountRecord();
        userAccountRecord.setName(createUserRequest.getUserName());
        userAccountRecord.setId(createUserRequest.getUserId());
        userAccountRecord.setAccountType(createUserRequest.getAccountType());
        userAccountRecord.setPassword(createUserRequest.getPassword());
        userAccountRecord.setEmail(createUserRequest.getEmail());


        userAccountRepository.save(userAccountRecord);
        //careerRepository.save(userAccountRecord);

        UserAccountInCareerResponse userAccountInCareerResponse = new UserAccountInCareerResponse();
        userAccountInCareerResponse.setUserId(createUserRequest.getUserId());
        userAccountInCareerResponse.setUserName(createUserRequest.getUserName());
        userAccountInCareerResponse.setAccountType(createUserRequest.getAccountType());
        userAccountInCareerResponse.setPassword(createUserRequest.getPassword());
        userAccountInCareerResponse.setEmail(createUserRequest.getEmail());

        return userAccountInCareerResponse;
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
