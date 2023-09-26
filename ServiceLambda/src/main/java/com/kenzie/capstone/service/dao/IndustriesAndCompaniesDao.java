package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.IndustriesAndCompanies;
import com.kenzie.capstone.service.model.IndustriesAndCompaniesRecord;

import java.util.List;

public class IndustriesAndCompaniesDao {

    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     *
     * @param mapper Access to DynamoDB
     */

    public IndustriesAndCompaniesDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public IndustriesAndCompanies storeIndustriesAndCompanies(IndustriesAndCompanies industriesAndCompanies) {
        try {
            mapper.save(industriesAndCompanies, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "name",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("name has already been used");
        }
        return industriesAndCompanies;
    }

    public List<IndustriesAndCompaniesRecord> getIndustriesAndCompanies(String name) {
        IndustriesAndCompaniesRecord industriesAndCompaniesRecord = new IndustriesAndCompaniesRecord();
        industriesAndCompaniesRecord.setName(name);

        DynamoDBQueryExpression<IndustriesAndCompaniesRecord> queryExpression = new DynamoDBQueryExpression<IndustriesAndCompaniesRecord>()
                .withHashKeyValues(industriesAndCompaniesRecord)
                .withConsistentRead(false);

        return mapper.query(IndustriesAndCompaniesRecord.class, queryExpression);
    }

    public IndustriesAndCompaniesRecord setIndustriesAndCompanies(String name, String description) {
        IndustriesAndCompaniesRecord industriesAndCompaniesRecord = new IndustriesAndCompaniesRecord();
        industriesAndCompaniesRecord.setName(name);
        industriesAndCompaniesRecord.setDescription(description);

        try {
            mapper.save(industriesAndCompaniesRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "name",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("name already exists");
        }

        return industriesAndCompaniesRecord;
    }

}
