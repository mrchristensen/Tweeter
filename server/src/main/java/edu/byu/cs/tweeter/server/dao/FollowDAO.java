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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.generators.UserGenerator;
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

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public FollowersResponse getFollowers(FollowersRequest request) {
        //todo
        return new FollowersResponse(UserGenerator.getNUsers(request.limit), true);
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        //todo
        return new FollowingResponse(UserGenerator.getNUsers(request.limit), true);
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