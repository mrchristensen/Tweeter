package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;

public class AuthTokenDAO {
    private static final String TableName = "authTokens";

    private static final String AuthTokenAttr = "auth_token";
    private static final String AliasAttr = "alias";
    private static final String TimestampAttr = "timestamp";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public boolean putAuthToken(String userAlias, String authToken, String timestamp) {
        System.out.println("putAuthToken: " + userAlias + " " + authToken);
        Table table = dynamoDB.getTable(TableName);

        //create a new user in the database
        Item item = new Item()
                .withPrimaryKey(AuthTokenAttr, authToken)
                .withString(AliasAttr, userAlias)
                .withString(TimestampAttr, timestamp);

        try {
            PutItemOutcome putResponse = table.putItem(item);

            //Successful register
            System.out.println("PutItem succeeded:\n" + putResponse.getPutItemResult());
            return true;
        }
        catch (Exception e) {
            //Unsuccessful Login
            System.err.println("Unable to add item: " + userAlias + " " + authToken);
            System.err.println(e.getMessage());
            return false;
        }
    }

    public AuthToken getAuthToken(String userAlias, String authTokenString) {
        System.out.println("getAuthToken: " + userAlias + " " + authTokenString);
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#authToken", AuthTokenAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":authToken", new AttributeValue().withS(authTokenString));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#authToken = :authToken")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(1);

        QueryResult queryResult;

        try {
            queryResult = amazonDynamoDB.query(queryRequest);
            System.out.println("Query Results: " + queryResult);
        } catch (AmazonDynamoDBException e) {
            System.out.println("AuthToken does not exist: " + authTokenString);
            e.printStackTrace();
            return null;
        }

        List<Map<String, AttributeValue>> items = queryResult.getItems();

        AuthToken foundAuthToken = null;
        if (items != null && items.size() > 0) {
            Map<String, AttributeValue> item = items.get(0);

            if(userAlias != null && !userAlias.equals(item.get(AliasAttr).getS())){
                System.out.println("User's alias doesn't match up with token: " + userAlias + " " + item.get(AliasAttr).getS());
                return null;
            }

            String timestamp = item.get(TimestampAttr).getS();

            foundAuthToken = new AuthToken(userAlias, authTokenString, timestamp);
            System.out.println("Found AuthToken: " + userAlias + " " + authTokenString + " " + timestamp);
        }

        return foundAuthToken;
    }

    public boolean deleteAuthToken(String authToken) {
        System.out.println("deleteAuthToken: " + authToken);
        Table table = dynamoDB.getTable(TableName);
        table.deleteItem(AuthTokenAttr, authToken);
        return true;
    }

}
