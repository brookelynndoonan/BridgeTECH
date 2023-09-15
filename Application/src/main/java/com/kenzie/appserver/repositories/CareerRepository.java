package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.CareerRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CareerRepository extends CrudRepository<CareerRecord,String> {
}
