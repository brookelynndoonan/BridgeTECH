package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.CompanyRecord;
import com.kenzie.appserver.repositories.model.IndustriesRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface IndustryRepository extends CrudRepository<IndustriesRecord, String> {


    List<IndustriesRecord> findByIndustryName(String industryName);

    IndustriesRecord findIndustryByName(String industryName);
}

