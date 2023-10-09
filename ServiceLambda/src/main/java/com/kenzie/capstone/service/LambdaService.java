package com.kenzie.capstone.service;

import com.kenzie.capstone.service.convertor.UserAccountConvertor;
import com.kenzie.capstone.service.dao.UserAccountsDao;
import com.kenzie.capstone.service.model.*;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;


public class LambdaService {

    private UserAccountsDao userAccountsDao;


    @Inject
    public LambdaService(UserAccountsDao userAccountsDao) {
        this.userAccountsDao = userAccountsDao;
    }

    public UserAccounts getUserAccounts(String id) {
        List<UserAccountRecord> records = userAccountsDao.getUserAccounts(id);
        if (records.size() > 0) {
            return new UserAccounts(records.get(0).getId(), records.get(0).getName(),
                    records.get(0).getAccountType(), records.get(0).getPassword(),
                    records.get(0).getEmail());
        }
        return null;
    }

    public UserAccounts setUserAccounts(String name, String accountType, String password,
    String email) {
        String id = UUID.randomUUID().toString();
        userAccountsDao.setUserAccounts(id, name, accountType, password, email);
        return new UserAccounts(id, name, accountType, password, email);
    }

    public UserAccountsResponse setUserAccounts(UserAccountsRequest request) {
        String id = UUID.randomUUID().toString();

        UserAccountRecord record = new UserAccountRecord();

        record = UserAccountConvertor.fromRequestToRecord(request);
        record.setId(id);
        userAccountsDao.storeUserAccount(record);

        return UserAccountConvertor.fromRecordToResponse(record);

    }
}
