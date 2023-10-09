package com.kenzie.appserver.service;

import com.kenzie.appserver.config.cachestore.CacheStoreCareer;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.UserAccountRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.repositories.CareerRepository;

import com.kenzie.appserver.service.model.Career;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.UserAccountRecord;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CareerService {
    private final CacheStoreCareer cache;
    private final CareerRepository careerRepository;
    private final UserAccountRepository userAccountRepository;
    private final LambdaServiceClient lambdaServiceClient;


    public CareerService(
            CareerRepository careerRepository,
            UserAccountRepository userAccountRepository,
            LambdaServiceClient lambdaServiceClient,
            CacheStoreCareer cache) {
        this.cache = cache;
        this.careerRepository = careerRepository;
        this.userAccountRepository = userAccountRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public List<Career> findAllCareers() {
        List<Career> careers = new ArrayList<>();

        Iterable<CareerRecord> careerIterator = careerRepository.findAll();
        for (CareerRecord record : careerIterator) {
            careers.add(new Career(record.getId(),
                    record.getCareerName(),
                    record.getLocation(),
                    record.getJobDescription(),
                    record.getCompanyDescription()));
        }

        return careers;
    }

    public Career findCareerById(String Id) {
        Career cachedCareer = cache.get(Id);
        if (cachedCareer != null) {
            return cachedCareer;
        }
        Optional<CareerRecord> careerRecordOptional = careerRepository.findById(Id);
        if (careerRecordOptional.isPresent()) {
            CareerRecord careerRecord = careerRecordOptional.get();
            Career careerFromBackendService = new Career(
                    careerRecord.getId(),
                    careerRecord.getCareerName(),
                    careerRecord.getLocation(),
                    careerRecord.getJobDescription(),
                    careerRecord.getCompanyDescription()
            );
            cache.add(Id, careerFromBackendService);
            return careerFromBackendService;
        }
        return null;
    }

    public CareerResponse addNewCareer(CareerCreateRequest careerCreateRequest) {

        CareerRecord careerRecord = createCareerRecordFromRequest(careerCreateRequest);

        careerRepository.save(careerRecord);

        return createCareerResponseFromRecord(careerRecord);
    }


    public void updateCareer(Career career) {
        if (careerRepository.existsById(career.getId())) {
            CareerRecord careerRecord = new CareerRecord();
            careerRecord.setId(career.getId());
            careerRecord.setCareerName(career.getCareerName());
            careerRecord.setLocation(career.getLocation());
            careerRecord.setJobDescription(career.getJobDescription());
            careerRecord.setCompanyDescription(career.getCompanyDescription());
            careerRepository.save(careerRecord);
            cache.evict(careerRecord.getId());
        }
    }


    public void deleteCareer(String id, String userId) {
        Optional<CareerRecord> career = careerRepository.findById(id);

        if (career.isPresent()) {
            CareerRecord careerRecord = career.get();

            if (careerRecord.getId().equals(userId)) {
                careerRepository.deleteById(id);
                cache.evict(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "You are not authorized to delete this career");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Career not found");
        }
    }

    public UserAccountInCareerResponse getUsers(String email) {
        Optional<UserAccountRecord> users = userAccountRepository.findById(email);

        if (users.isPresent()) {
            UserAccountInCareerResponse userAccountInCareerResponse = new UserAccountInCareerResponse();
            userAccountInCareerResponse.setEmail(users.get().getEmail());
            userAccountInCareerResponse.setPassword(users.get().getPassword());

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

        String careerId = UUID.randomUUID().toString();

        CareerRecord record = new CareerRecord();

        record.setId(careerId);
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
