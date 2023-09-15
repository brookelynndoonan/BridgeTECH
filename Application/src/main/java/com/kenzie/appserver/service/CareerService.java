package com.kenzie.appserver.service;


import com.kenzie.appserver.controller.model.CareerCreateRequest;
import com.kenzie.appserver.controller.model.CareerResponse;
import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.appserver.repositories.CareerRepository;
import com.kenzie.appserver.service.model.Career;

// import com.kenzie.capstone.service.client.LambdaServiceClient; Once lambdas are made we'll update this import
 // import com.kenzie.capstone.service.model.ExampleData; ^^
import org.checkerframework.checker.units.qual.C;
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

    public CareerService(CareerRepository careerRepository){
        this.careerRepository = careerRepository;
    }

    public List<CareerResponse>  findAllCareers (){

        List<CareerRecord> recordList = StreamSupport.stream(careerRepository.findAll().spliterator(),true).collect(Collectors.toList());

         return recordList.stream()
                 .map(record-> createCareerResponseFromRecord(record))
                 .collect(Collectors.toList());
    }

    public CareerResponse findCareerById(String id){

        CareerResponse careerInfo = careerRepository
                .findById(id)
                .map(careerRecord -> createCareerResponseFromRecord(careerRecord))
                .orElse(null);

        return careerInfo;
    }

    public CareerResponse addNewCareer(CareerCreateRequest careerCreateRequest){

        CareerRecord careerRecord = createCareerRecordFromRequest(careerCreateRequest);

        careerRepository.save(careerRecord);

        return createCareerResponseFromRecord(careerRecord);


    }

    public CareerResponse updateCareer(String id, String name,String location, String jobDescription, String companyDescription){

        Optional<CareerRecord> careerExist = careerRepository.findById(id);

        if(careerExist.isEmpty()){
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


    public void deleteCareer(String id){ careerRepository.deleteById(id);}

    // PRIVATE HELPER METHODS
    private CareerRecord createCareerRecordFromRequest(CareerCreateRequest request){

        CareerRecord record = new CareerRecord();

        record.setId(UUID.randomUUID().toString());
        record.setCareerName(request.getName());
        record.setLocation(request.getLocation());
        record.setJobDescription(request.getJobDescription());
        record.setCompanyDescription(request.getCompanyDescription());

        return record;
    }

    private CareerResponse createCareerResponseFromRecord(CareerRecord careerRecord){

        CareerResponse response = new CareerResponse();

        response.setId(careerRecord.getId());
        response.setName(careerRecord.getCareerName());
        response.setLocation(careerRecord.getLocation());
        response.setJobDescription(careerRecord.getJobDescription());
        response.setCompanyDescription(careerRecord.getCompanyDescription());

        return response;
    }



}
