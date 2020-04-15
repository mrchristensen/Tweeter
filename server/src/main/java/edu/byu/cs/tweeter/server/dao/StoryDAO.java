package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.generators.StatusGenerator;
import edu.byu.cs.tweeter.server.resources.ResultsPage;
import edu.byu.cs.tweeter.server.resources.StatusResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class StoryDAO {
    private static final String TableName = "stories";

    private static final String AliasAttr = "alias";
    private static final String TimestampAttr = "timestamp";
    private static final String MessageAttr = "message";
    private static final String ProfileURLAttr = "image_url";
    private static final String FirstNameAttr = "first_name";
    private static final String LastNameAttr = "last_name";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public boolean putStatus(Status status) {
        System.out.println("putStatus: " + status.getMessageBody() + "for: " + status.getUser());
        Table table = dynamoDB.getTable(TableName);

        //create a new user in the database
        Item item = new Item()
                .withPrimaryKey(AliasAttr, status.getUser().getAlias())
                .withPrimaryKey(TimestampAttr, status.getDate())
                .withString(MessageAttr, status.getMessageBody())
                .withString(ProfileURLAttr, status.getUser().getImageUrl())
                .withString(FirstNameAttr, status.getUser().getFirstName())
                .withString(LastNameAttr, status.getUser().getLastName());

        try {
            PutItemOutcome putResponse = table.putItem(item);

            //Successful register
            System.out.println("PutItem succeeded:\n" + putResponse.getPutItemResult());
            return true;
        }
        catch (Exception e) {
            //Unsuccessful Login
            System.err.println("Unable to add item: " + status);
            System.err.println(e.getMessage());
            return false;
        }
    }

    public StatusResultsPage getStory(String userAlias, int pageSize, String lastStatusTimestamp) {
        System.out.println("getStory: for " + userAlias + ", lastStatusTimestamp: " + lastStatusTimestamp);
        StatusResultsPage result = new StatusResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#userAlias", AliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":userAlias", new AttributeValue().withS(userAlias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#userAlias = :userAlias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize)
                .withScanIndexForward(false); //Descending order

        if (isNonEmptyString(lastStatusTimestamp)) {
            System.out.println("adding a start key of: " + lastStatusTimestamp);
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AliasAttr, new AttributeValue().withS(userAlias));
            startKey.put(TimestampAttr, new AttributeValue().withS(lastStatusTimestamp));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query result: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){

                String firstName = item.get(FirstNameAttr).getS();
                String lastName = item.get(LastNameAttr).getS();
                String alias = item.get(AliasAttr).getS();
                String profileURL = item.get(ProfileURLAttr).getS();
                User user = new User(firstName, lastName, alias, profileURL);

                String timestamp = item.get(TimestampAttr).getS();
                String statusMessage = item.get(MessageAttr).getS();
                Status status = new Status(user, timestamp, statusMessage);

                result.addStatus(status);
                System.out.println("Added: " + status);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(TimestampAttr).getS());
            System.out.println("lastKey = " + lastKey.get(TimestampAttr).getS());
        }

        return result;
    }

}