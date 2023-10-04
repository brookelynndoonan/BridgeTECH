package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.service.CareerService;
import com.kenzie.appserver.service.ExampleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/user/{id}")
    public ResponseEntity<CareerResponse> getUserAccounts(@PathVariable("id") String userId) {
        CareerResponse careerResponse = careerService.getUsers(userId);

        if (careerResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(careerResponse);
    }

    @PostMapping("/user")
    public ResponseEntity<CareerResponse> createUser(@RequestBody CareerCreateRequest careerCreateRequest) {
        try {
            String name = careerCreateRequest.getName();
            String accountType = careerCreateRequest.getAccountType();
            String password = careerCreateRequest.getPassword();

            CareerResponse careerResponse = careerService.createUser(name, accountType, password);

            return ResponseEntity.created(URI.create("/Career/user/" + careerResponse.getUserId())).body(careerResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user account", e);
        }
    }

}





