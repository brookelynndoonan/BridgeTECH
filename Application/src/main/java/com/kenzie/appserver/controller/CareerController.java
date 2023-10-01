package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.service.CareerService;
import com.kenzie.appserver.service.ExampleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Career")
public class CareerController {
    private CareerService careerService;

    CareerController(CareerService careerService) {
        this.careerService = careerService;
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
    public ResponseEntity<CareerResponse> updateCareer(@PathVariable("Id")
                                                       @RequestBody CareerCreateRequest careerCreateRequest) {
        CareerResponse response = careerService.updateCareer(careerCreateRequest.getName(), careerCreateRequest.getId(),
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

    //Look into more about how we are deleting things, based off User criteria.
    @DeleteMapping("/{Id}")
    public ResponseEntity deleteCustomerById(@PathVariable("Id") String careerId,
                                             @RequestParam("userId") String userId) {
        careerService.deleteCareer(careerId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CareerResponse> getUserAccounts(@PathVariable String userId) {
        CareerResponse careerResponse = careerService.getUsers(userId);

        if (careerResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(careerResponse);
    }
}





