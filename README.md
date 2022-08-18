# Tweeter

This repo holds an Android app that mimics the functionality of Twitter using Java and AWS (API Gateway, Lambda Functions, DynamoDB, SQS).

The app supports:
- User registration/login (with secure authentication and best practices, such as password hashing and salting and invalidating auth tokens), profile pictures, and bios
- Posting statuses (with urls and @mentions)
- User feed (a collection of all posts of the user's followed list)
- User stories (a collection of all a given user's posts)
- Following/Unfollowing

(See a UML description of these relationships [here](documentation/Tweeter%20-%20UML%20Class%20Diagram%20-%20Project%20Conceptual%20Overview.pdf))

## Frontend

A Model View Presenter architecture (MVP) is used to separate logic from the backend:

![](documentation/class-documentation/Milestone%202%20Architecture%20Diagram.jpg)

### Model

The model layer contains plain old Java objects (POJO) that simply store the required data:

![](documentation/Tweeter%20-%20UML%20Class%20Diagram%20-%20Client%20Model%20Layer.jpg)

### View/Presenter

The view and presenter handle the presentation of the app and connection to the backend (respectively):

![](https://github.com/mrchristensen/Tweeter/blob/master/documentation/Tweeter%20-%20UML%20Class%20Diagram%20-%20User%20Interface.jpg)

### Example Login Sequence

For an example of how data is sent through the different layers, see the Login Sequence Diagram:
![](documentation/Tweeter%20-%20UML%20Sequence%20Diagram%20-%20Login.jpg)

## Backend

In the backend, we use Gateway to create a REST API with the required endpoints.
The endpoints take the data in the API call and run Lambda Functions.
The results of the Lambda Functions are then bundled into the API call's response objects.

The Gateway API can be seen in the [Tweeter-dev-swagger.json](server/src/main/java/edu/byu/cs/tweeter/server/Tweeter-dev-swagger.json).

![](documentation/class-documentation/Milestone%203%20Architecture%20Diagram.jpg)

### DynamoDB

We use DynamoDB to store the user information, posts, and feeds.
To see how the databases are formatted, see [documentation/DynamoDB Table Descriptions.pdf](documentation/Tweeter%20-%20DynamoDB%20Table%20Descriptions.pdf).

### SQS

Finally, we use Simple Queue Service (SQS) to pin up multiple lambda functions to update at the feeds after a user posts a new status.
Two queues are spun up: one to get the followers of the person who postest the status.
The second queue is given those user ids and handles updating the feeds of those users with the new post.

Because two queues are used the following performance requirements are satisfied:
- The perceived latency of the create new status operation (from the perspective of the author) is to be less than 1000 milliseconds.
- When a new status is created, that status is visible in the feeds of all of the followers of the author within 120 seconds, for authors with up to 10K followers.
- Each page of a user's feed is returned in less than 1000 milliseconds, from the perspective of the user.

![](documentation/class-documentation/Milestone%204%20Post%20Status%20Architecture%20Diagram.jpg)
