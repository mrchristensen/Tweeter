package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.List;
import java.util.Map;

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

    public boolean feedStatusBatchWrite(Status status, List<User>followers) {
        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (User follower : followers) {
            Item item = new Item()
                    .withPrimaryKey(FeedAliasAttr, follower.getAlias(), TimestampAttr, status.getDate())
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
}
