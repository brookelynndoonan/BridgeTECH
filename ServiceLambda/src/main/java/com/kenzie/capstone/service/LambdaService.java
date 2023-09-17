package com.kenzie.capstone.service;

import com.kenzie.capstone.service.convertor.UserAccountConvertor;
import com.kenzie.capstone.service.dao.UserAccountsDao;
import com.kenzie.capstone.service.model.*;
import com.kenzie.capstone.service.dao.ExampleDao;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private ExampleDao exampleDao;
    private UserAccountsDao userAccountsDao;

   /* @Inject
    public LambdaService(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    */

    @Inject
    public LambdaService(UserAccountsDao userAccountsDao) { this.userAccountsDao = userAccountsDao;}

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ExampleRecord record = exampleDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }

    public UserAccounts getUserAccounts(String id) {
        List<UserAccountRecord> records = userAccountsDao.getUserAccounts(id);
        if (records.size() > 0) {
            return new UserAccounts(records.get(0).getId(),records.get(0).getName(),records.get(0).getAccountType(),records.get(0).getPassword());
        }
        return null;
    }

    public UserAccounts setUserAccounts(String name, String accountType, String password) {
        String id = UUID.randomUUID().toString();
        userAccountsDao.setUserAccounts(id, name, accountType, password);
        return new UserAccounts(id, name, accountType, password);
    }

    public UserAccountsResponse setUserAccounts(UserAccountsRequest request){
        String id = UUID.randomUUID().toString();

        UserAccountRecord record = new UserAccountRecord();

        record = UserAccountConvertor.fromRequestToRecord(request);
        record.setId(id);
        userAccountsDao.storeUserAccount(record);

        UserAccountsResponse response = UserAccountConvertor.fromRecordToResponse(record);
        return response;


    }
}
