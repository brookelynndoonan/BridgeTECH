package com.kenzie.capstone.service.lambda;


import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.convertor.JsonStringToUserAccountConvertor;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.UserAccounts;
import com.kenzie.capstone.service.model.UserAccountsRequest;
import com.kenzie.capstone.service.model.UserAccountsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SetUserAccounts implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        JsonStringToUserAccountConvertor jsonStringToUserAccountConvertor = new JsonStringToUserAccountConvertor();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        LambdaService lambdaService = serviceComponent.provideLambdaService();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);


        try {
            UserAccountsRequest request = jsonStringToUserAccountConvertor.convert(input.getBody());
            UserAccountsResponse response1 = lambdaService.setUserAccounts(request);

            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(response1));

        } catch (Exception e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.getMessage()));
        }
    }
}
