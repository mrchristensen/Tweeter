package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import edu.byu.cs.tweeter.server.dao.generators.StatusGenerator;
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
public class StatusDAO {
    private static final String TableName = "stories";
//    private static final String IndexName = "visits-index";

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

    public FeedResponse getFeed(FeedRequest request) {
        //TODO: Implement actual functionality once Databases are implemented
        String FEMALE_IMAGE_URL = "https://i.imgur.com/LiVHSFn.png";
        return new FeedResponse(StatusGenerator.getNStatuses(request.getLimit(), new User("fname1", "lname1", "tempAlias1", FEMALE_IMAGE_URL)), true);
    }

    public StoryResponse getStory(StoryRequest request) {
        //TODO: Implement actual functionality once Databases are implemented
        return new StoryResponse(StatusGenerator.getNStatuses(request.getLimit(), request.getUser()), true);
    }

}