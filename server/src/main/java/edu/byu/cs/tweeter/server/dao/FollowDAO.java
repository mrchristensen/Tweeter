package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.generators.UserGenerator;
import edu.byu.cs.tweeter.server.resources.ResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class FollowDAO {
    private static final String TableName = "follows";
//    private static final String IndexName = "visits-index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    private static final String FollowsIndex = "follows_index";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public ResultsPage getFollowers(String followee, int pageSize, String lastFollower) {
        System.out.println("getFollowers: for " + followee + ", lastFollower: " + lastFollower);
        ResultsPage result = new ResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#followee", FolloweeAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee", new AttributeValue().withS(followee));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withIndexName(FollowsIndex)
                .withKeyConditionExpression("#followee = :followee")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollower)) {
            System.out.println("adding a start key of: " + lastFollower);
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, new AttributeValue().withS(followee));
            startKey.put(FollowerAttr, new AttributeValue().withS(lastFollower));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query result: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                String follower = item.get(FollowerAttr).getS();
                result.addValue(follower);
                System.out.println("Added: " + follower);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(FollowerAttr).getS());
            System.out.println("lastKey = " + lastKey.get(FollowerAttr).getS());
        }

        return result;
    }

    public ResultsPage getFollowees(String user, int pageSize, String lastFollowee) {
        System.out.println("getFollowers: for " + user + ", lastFollowee: " + lastFollowee);
        ResultsPage result = new ResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#user", FollowerAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":user", new AttributeValue().withS(user));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#user = :user")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollowee)) {
            System.out.println("adding a start key of: " + lastFollowee);
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, new AttributeValue().withS(user));
            startKey.put(FolloweeAttr, new AttributeValue().withS(lastFollowee));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query result: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                String followee = item.get(FolloweeAttr).getS();
                result.addValue(followee);
                System.out.println("Added: " + followee);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(FolloweeAttr).getS());
            System.out.println("lastKey = " + lastKey.get(FolloweeAttr).getS());
        }

        return result;
    }

    public boolean getFollow(String follower, String followee){
        //See if follower is following the followee
        System.out.println("getFollow: \"" + follower + "\" following \"" + followee + "\"?");

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#follower", FollowerAttr);
        attrNames.put("#followee", FolloweeAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower", new AttributeValue().withS(follower));
        attrValues.put(":followee", new AttributeValue().withS(followee));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#follower = :follower and #followee = :followee")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(1);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query Results: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();

        if (items != null && items.size() > 0) {
            System.out.println(follower + "\" follows \"" + followee + "\"");
            return true;
        }
        else{
            System.out.println(follower + "\" does not follow \"" + followee + "\"");
            return false;
        }
    }

    public boolean deleteFollow(String follower, String followee){
        System.out.println("deleteFollow: \"" + follower +  "\" no longer follows \"" + followee + "\"");

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#follower", FollowerAttr);
        attrNames.put("#followee", FolloweeAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower", new AttributeValue().withS(follower));
        attrValues.put(":followee", new AttributeValue().withS(followee));

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(FollowerAttr, follower, FolloweeAttr, followee);
//                .withExpressionAttributeNames(attrNames)
//                .withExpressionAttributeValues(attrValues);

        Table table = dynamoDB.getTable(TableName);
        DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
        System.out.println(outcome);

        return true;
    }

    public boolean putFollow(String follower, String followee){
        System.out.println("putFollow: \"" + follower + "\" following \"" + followee + "\"?");

        Table table = dynamoDB.getTable(TableName);

        //create a new user in the database
        Item item = new Item()
                .withPrimaryKey(FollowerAttr, follower)
                .withPrimaryKey(FolloweeAttr, followee);

        try {
            PutItemOutcome putResponse = table.putItem(item);

            //Successful register
            System.out.println("PutItem succeeded:\n" + putResponse.getPutItemResult());
            return true;
        }
        catch (Exception e) {
            //Unsuccessful Login
            System.err.println("Unable to add follow");
            System.err.println(e.getMessage());
            return false;
        }
    }

}