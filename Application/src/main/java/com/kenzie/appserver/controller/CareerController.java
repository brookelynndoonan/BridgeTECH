package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.service.CareerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/Career")
public class CareerController {
    private CareerService careerService;
    private CareerRepository careerRepository;

    CareerController(CareerService careerService, CareerRepository careerRepository) {
        this.careerService = careerService;
        this.careerRepository = careerRepository;
    }


    @PostMapping
    public ResponseEntity<CareerResponse> addNewCareer(@RequestBody CareerCreateRequest careerCreateRequest) {

        if (careerCreateRequest.getName() == null || careerCreateRequest.getName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Career Name");
        }

        CareerResponse response = careerService.addNewCareer(careerCreateRequest);


        return ResponseEntity.created(URI.create("/careers/" + response.getId())).body(response);

    }


    @PostMapping("/{Id}")
    public ResponseEntity<CareerResponse> updateCareer(@PathVariable("Id") String id,
                                                       @RequestBody CareerCreateRequest careerCreateRequest) {
        CareerResponse response = careerService.updateCareer(careerCreateRequest.getName(), id,
                careerCreateRequest.getLocation(), careerCreateRequest.getCompanyDescription(),
                careerCreateRequest.getJobDescription());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CareerResponse>> getAllCareers() {
        List<CareerResponse> careers = careerService.findAllCareers();
        if (careers == null || careers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(careers);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<CareerResponse> searchCareerById(@PathVariable("Id") String careerId) {
        CareerResponse careerResponse = careerService.findCareerById(careerId);
        if (careerResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(careerResponse);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteCustomerById(@PathVariable("Id") String careerId,
                                             @RequestParam("userId") String userId) {
        careerService.deleteCareer(careerId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{Id}")
    public ResponseEntity<UserAccountInCareerResponse> getUserAccounts(@PathVariable("Id") String userId) {
        UserAccountInCareerResponse userAccountInCareerResponse = careerService.getUsers(userId);

        if (userAccountInCareerResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userAccountInCareerResponse);
    }

    @PostMapping("/userAccounts/user")
    public ResponseEntity<UserAccountInCareerResponse> createUser(@RequestBody UserAccountInCareerRequest userAccountInCareerRequest) {
        try {
            String name = userAccountInCareerRequest.getUserName();
            String accountType = userAccountInCareerRequest.getAccountType();
            String password = userAccountInCareerRequest.getPassword();
            String userId = randomUUID().toString();

            UserAccountInCareerResponse userAccountInCareerResponse = careerService.createUser(userAccountInCareerRequest);

            return ResponseEntity.created(URI.create("/Career/user/")).body(userAccountInCareerResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user account", e);
        }
    }

}





