package com.kenzie.capstone.service.convertor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exception.InvalidDataException;
import com.kenzie.capstone.service.model.UserAccountsRequest;

public class JsonStringToUserAccountConvertor {

    public UserAccountsRequest convert(String body){
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            UserAccountsRequest userAccountsRequest = gson.fromJson(body, UserAccountsRequest.class);
            return userAccountsRequest;
        } catch (Exception e) {
            throw new InvalidDataException("Referral could not be deserialized");
        }

    }
}
