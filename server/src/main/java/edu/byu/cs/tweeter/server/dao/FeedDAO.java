package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.resources.StatusResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;

public class FeedDAO {
    private static final String TableName = "feeds";

    private static final String FeedAliasAttr = "user_feed_alias";

    private static final String TimestampAttr = "timestamp";
    private static final String MessageAttr = "message";

    private static final String PosterAliasAttr = "poster_alias";
    private static final String PosterFirstNameAttr = "poster_first_name";
    private static final String PosterLastNameAttr = "poster_last_name";
    private static final String PosterProfileURLAttr = "poster_image_url";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public boolean feedStatusBatchWrite(Status status, List<String> followerAliases) {
        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (String followerAlias : followerAliases) {
            Item item = new Item()
                    .withPrimaryKey(FeedAliasAttr, followerAlias, TimestampAttr, status.getDate())
                    .withString(MessageAttr, status.getMessageBody())
                    .withString(PosterAliasAttr, status.getUser().getAlias())
                    .withString(PosterFirstNameAttr, status.getUser().getFirstName())
                    .withString(PosterLastNameAttr, status.getUser().getLastName())
                    .withString(PosterProfileURLAttr, status.getUser().getImageUrl());
            items.addItemToPut(item);
        }

        System.out.println("About to write a batch: " + items);
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        System.out.println("Wrote User Batch: " + outcome);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            System.out.println("Outcome has unprocessed items: " + outcome);
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            System.out.println("Wrote more Users: " + outcome);
        }

        return true;
    }

    public StatusResultsPage getFeed(String userAlias, int pageSize, String lastStatusTimestamp) {
        System.out.println("getFeed: for " + userAlias + ", lastStatusTimestamp: " + lastStatusTimestamp);
        StatusResultsPage result = new StatusResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#FeedAlias", FeedAliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":FeedAlias", new AttributeValue().withS(userAlias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#FeedAlias = :FeedAlias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize)
                .withScanIndexForward(false); //Descending order

        if (isNonEmptyString(lastStatusTimestamp)) {
            System.out.println("adding a start key of: " + lastStatusTimestamp);
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FeedAliasAttr, new AttributeValue().withS(userAlias));
            startKey.put(TimestampAttr, new AttributeValue().withS(lastStatusTimestamp));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query result: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){

                String firstName = item.get(PosterFirstNameAttr).getS();
                String lastName = item.get(PosterLastNameAttr).getS();
                String alias = item.get(PosterAliasAttr).getS();
                String profileURL = item.get(PosterProfileURLAttr).getS();
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
