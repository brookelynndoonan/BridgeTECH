package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.CareerRecord;
import com.kenzie.capstone.service.model.UserAccountRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserAccountRepository extends CrudRepository<UserAccountRecord,String> {
}
