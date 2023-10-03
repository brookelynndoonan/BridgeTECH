package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.model.UserAccounts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetUserAccounts implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;

    public GetUserAccounts() {
        dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(dynamoDB);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson(input));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);

        String id = input.getPathParameters().get("id");

        if (id == null || id.length() == 0) {
            return response.withStatusCode(400).withBody("Id is invalid");
        }

        try {

            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":userId", new AttributeValue().withS(id));

            QueryRequest queryRequest = new QueryRequest()
                    .withTableName("LambdaUserAccounts")
                    .withIndexName("IdIndex")
                    .withKeyConditionExpression("id = :userId")
                    .withExpressionAttributeValues(expressionAttributeValues);

            QueryResult queryResult = dynamoDB.query(queryRequest);

            if (queryResult.getCount() == 1) {
                UserAccounts userAccounts = dynamoDBMapper.load(UserAccounts.class, queryResult.getItems().get(0));
                String output = gson.toJson(userAccounts);
                return response.withStatusCode(200).withBody(output);
            } else {
                return response.withStatusCode(404).withBody("User not found");
            }

        } catch (Exception e) {
            return response.withStatusCode(500).withBody(gson.toJson(e.getMessage()));
        }
    }
}