# Tweeter

This repo hold an Android app that mimics the functionality of Twitter using Java and AWS (API Gateway, Lambda Functions, DynamoDB, SQS).

The app supports:
- User registration/login (with secure authentication and best practices, such as password hashing and salting), profile pictures, and bios
- Posting statuses (with urls and @mentions)
- User feed (a collection of all posts for the the user's followed list)
- User stories (a collection of all a given user's posts)
- Following/Unfollowing

(See a UML description of these relationships [here](documentation/Tweeter%20-%20UML%20Class%20Diagram%20-%20Project%20Conceptual%20Overview.pdf))

## Frontend

## Backend

### DynamoDB
To see how the databases are formatted, see [documentation/DynamoDB Table Descriptions.pdf](documentation/Tweeter%20-%20DynamoDB%20Table%20Descriptions.pdf)

### SQS
