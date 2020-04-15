package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A DAO for accessing the users database.
 */
public class UserDAO {

    private static final String TableName = "users";
//    private static final String IndexName = "visits-index";

    private static final String AliasAttr = "alias";
    private static final String PasswordAttr = "password";
    private static final String FirstNameAttr = "first_name";
    private static final String LastNameAttr = "last_name";
    private static final String ImageURLAttr = "image_url";

    // DynamoDB client
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);


    public boolean putUser(String alias, String password, String firstName, String lastName, String imageURL) {
        System.out.println("putUser: " + alias);
        Table table = dynamoDB.getTable(TableName);

        //create a new user in the database
        Item item = new Item()
                .withPrimaryKey(AliasAttr, alias)
                .withString(PasswordAttr, password)
                .withString(FirstNameAttr, firstName)
                .withString(LastNameAttr, lastName)
                .withString(ImageURLAttr, imageURL);

        try {
            PutItemOutcome putResponse = table.putItem(item);

            //Successful register
            System.out.println("PutItem succeeded:\n" + putResponse.getPutItemResult());
            return true;
        }
        catch (Exception e) {
            //Unsuccessful Login
            System.err.println("Unable to add item: " + alias);
            System.err.println(e.getMessage());
            return false;
        }
    }

    public User getUser(String alias){
        System.out.println("getUser: " + alias);
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#al", AliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(alias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#al = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(1);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query Results: " + queryResult);
        List<Map<String, AttributeValue>> items = queryResult.getItems();

        User foundUser = null;
        if (items != null && items.size() > 0) {
            Map<String, AttributeValue> item = items.get(0);

            if(!alias.equals(item.get(AliasAttr).getS())){
                System.out.println("User's alias' don't match up: " + alias + " " + item.get(AliasAttr).getS());
            }

            String firstName = item.get(FirstNameAttr).getS();
            String lastName = item.get(LastNameAttr).getS();
            String imageURL = item.get(ImageURLAttr).getS();

            foundUser = new User(firstName, lastName, alias, imageURL);
            System.out.println(firstName + " " + lastName + " " + alias + " " + imageURL);
        }

        return foundUser;
    }

    public boolean validateLogin(String alias, String password) {
        System.out.println("validateLogin: " + alias);

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", AliasAttr);
        attrNames.put("#password", PasswordAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(alias));
        attrValues.put(":password", new AttributeValue().withS(password));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#alias = :alias")
                .withFilterExpression("#password = :password")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(1);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        System.out.println("Query Results: " + queryResult);

        if(queryResult.getItems().size() > 0){
            return true;
        }
        return false;
    }

    public boolean userBatchWrite(List<String> userAliases) {
        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (String userAlias : userAliases) {
            Item item = new Item()
                    .withPrimaryKey(AliasAttr, userAlias)
                    .withString(PasswordAttr, "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86") //"password" hashed
                    .withString(FirstNameAttr, "firstName")
                    .withString(LastNameAttr, "lastName")
                    .withString(ImageURLAttr, "https://BUCKET_NAME.s3.us-west-2.amazonaws.com/IMAGE_URL_API");
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