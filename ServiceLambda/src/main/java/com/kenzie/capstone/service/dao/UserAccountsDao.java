package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.UserAccountRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;


import java.util.List;

public class UserAccountsDao {

    private DynamoDBMapper mapper;

    public UserAccountsDao(DynamoDBMapper dbMapper) {
        this.mapper = dbMapper;
    }

    public UserAccountRecord storeUserAccount(UserAccountRecord account) {

        try {
            mapper.save(account, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "Id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return account;
    }


    public List<UserAccountRecord> getUserAccounts(String id) {
        UserAccountRecord accounts = new UserAccountRecord();
        accounts.setId(id);

        DynamoDBQueryExpression<UserAccountRecord> queryExpression = new DynamoDBQueryExpression<UserAccountRecord>()
                .withHashKeyValues(accounts)
                .withConsistentRead(false);

        return mapper.query(UserAccountRecord.class, queryExpression);
    }

    public UserAccountRecord setUserAccounts(String id, String name, String accountType, String password) {
        UserAccountRecord accountsRecord = new UserAccountRecord();
        accountsRecord.setId(id);
        accountsRecord.setName(name);
        accountsRecord.setAccountType(accountType);
        accountsRecord.setPassword(password);

        try {
            mapper.save(accountsRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return accountsRecord;
    }


}
