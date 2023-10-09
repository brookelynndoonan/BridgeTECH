package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerRequestResponse.CareerResponse;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerRequest;
import com.kenzie.appserver.controller.model.UserAccountInCareerRequestResponse.UserAccountInCareerResponse;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.service.CareerService;
import com.kenzie.appserver.service.model.Career;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


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


    @PutMapping
    public ResponseEntity<CareerResponse> updateCareer(@RequestBody CareerCreateRequest careerCreateRequest) {
        Career career = new Career(careerCreateRequest.getId(),
                careerCreateRequest.getName(),
                careerCreateRequest.getLocation(),
                careerCreateRequest.getJobDescription(),
                careerCreateRequest.getCompanyDescription());
        careerService.updateCareer(career);

        CareerResponse careerResponse = createCareerResponse(career);

        return ResponseEntity.ok(careerResponse);
    }

    @GetMapping
    public ResponseEntity<List<CareerResponse>> getAllCareers() {
        List<Career> career = careerService.findAllCareers();

        if (career == null || career.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CareerResponse> response = new ArrayList<>();
        for (Career careers : career) {
            response.add(this.createCareerResponse(careers));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<CareerResponse> searchCareerById(@PathVariable String Id) {
        Career career = careerService.findCareerById(Id);

        if (career == null) {
            return ResponseEntity.notFound().build();
        }

        CareerResponse careerResponse = createCareerResponse(career);
        return ResponseEntity.ok(careerResponse);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity deleteCustomerById(@PathVariable("Id") String careerId,
                                             @RequestParam("userId") String userId) {
        careerService.deleteCareer(careerId, userId);
        return ResponseEntity.ok().build();
    }

    // Blake Helped me write this method.
    @GetMapping("/userAccounts/user/{Id}")
    public ResponseEntity<UserAccountInCareerResponse> getUserAccounts(@PathVariable("Id") String Id) {
        UserAccountInCareerResponse userAccountInCareerResponse = careerService.getUsers(Id);

        if (userAccountInCareerResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userAccountInCareerResponse);
    }

    // Blake Helped me write this method.
    @PostMapping("/userAccounts/user")
    public ResponseEntity<UserAccountInCareerResponse> createUser(@RequestBody UserAccountInCareerRequest userAccountInCareerRequest) {
        try {
            UserAccountInCareerResponse userAccountInCareerResponse = careerService.createUser(userAccountInCareerRequest);

            URI location = URI.create("/Career/userAccounts/user/" + userAccountInCareerResponse.getUserId());
            return ResponseEntity.created(location).body(userAccountInCareerResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user account", e);
        }
    }


    private CareerResponse createCareerResponse(Career career) {
        CareerResponse careerResponse = new CareerResponse();
        careerResponse.setId(career.getId());
        careerResponse.setLocation(career.getLocation());
        careerResponse.setName(career.getCareerName());
        careerResponse.setJobDescription(career.getJobDescription());
        careerResponse.setCompanyDescription(career.getCompanyDescription());
        return careerResponse;
    }

}





