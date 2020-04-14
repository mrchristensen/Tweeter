package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * A DAO for accessing the users database.
 */
public class UserDAO {

    private static final String MALE_IMAGE_URL = "https://i.imgur.com/IMAGE_URL_API.png";
    private static final String FEMALE_IMAGE_URL = "https://i.imgur.com/LiVHSFn.png";

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
        Map<String, String> attrNames = new HashMap<String, String>();
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

    public FindUserResponse findUser(FindUserRequest request) { //todo delete this method
        String userAlias = request.getUserAlias();

        //TODO: Find the actual user once databases are implemented (instead of looking for hardcoded users)
        if(userAlias.contains("tempAlias") && userAlias.charAt(userAlias.length() - 1) - '0' <= 9){  //Check for tempAlias's 0-9
            char i = userAlias.charAt(userAlias.length() - 1); //get number at the end

            User tempUser = new User("fname" + i, "lname" + i, userAlias,
                    FEMALE_IMAGE_URL);
            return new FindUserResponse(true, tempUser);

        }
        else if (userAlias.equals("@test")){
            return new FindUserResponse(true, new User("Josh", "Smith", "@test", MALE_IMAGE_URL));
        }
        else{
            return new FindUserResponse(false, userAlias);
        }
    }


    public boolean validateLogin(String alias, String password) {
        System.out.println("validateLogin: " + alias);

        Map<String, String> attrNames = new HashMap<String, String>();
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
}