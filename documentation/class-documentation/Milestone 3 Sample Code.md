# Milestone 3 Sample Code
## Accessing the Sample Code
Sample code showing the 'following' functionality for milestone 3 is available on Github at the following URL: https://github.com/jerodw/tweeter-samples.  Download and open the zip file or clone the repo to get the code.

The sample project has multiple branches. The code for milestone 3 is in the 'server-sample' branch. From the command line, you can see what branch your project is on with the 'git branch' command. To checkout (make current) the branch containing the server code use 'git checkout server-sample'. Android Studio and other IDEs have built-in support for switching branches.

You can also download or clone the code just for the 'server-sample' branch from here: https://github.com/jerodw/tweeter-samples/tree/server-sample.

## Milestone 3 Sample Code Overview
The milestone 3 code has three modules. The 'app' module contains the code for the Android client. The 'server' module contains the code for the 'GetFollowing' AWS lambda function and the code the lambda calls. The 'shared' module contains code that is used by both the 'app' and 'server' modules. The code was divided into modules for milestone 3 so a .jar file could be generated that contains only server code for use when deploying the AWS lambdas. The steps I followed to migrate the code from the milestone 2 sample to a starting point for the milestone 3 sample are described in a later section of this document. The main additions in the milestone 3 code are the following:

* Created the 'GetFollowing' AWS lambda (in the server module) for getting the 'following' for a user.
* Added the ClientCommunicator and Serializer classes.
* Changed the ServerFacade class so instead of generating dummy data, it uses the ClientCommunicator to invoke the lambda function to get the data.
* Added a FollowingDAO class. This class will access a DynamoDB database in milestone 4, but for milestone 3, it returns the dummy data that the ServerFacade returned in milestone 2.
* Converted the FollowingService class to follow the proxy pattern with a FollowingService interface in the shared module, a FollowingServiceProxy class in the client module and a FollowServiceImpl class in the server module. The FollowingServiceProxy class is a remote proxy for FollowingServiceImpl.
  * The FollowingServiceImpl class is invoked by the GetFollowing lambda. It calls FollowingDAO to get the data.
* Made changes necessary to propagate the IOException, which can now be thrown by the ClientCommunicator, up to the view. The view logs the exception and displays it to the user as a toast.


## Migrating from the Milestone 2 to the Milestone 3 Code
The milestone 3 sample code was created from the milestone 2 code by creating a new branch and making two major changes: 1) some refactoring to support the generation of a server specific jar file for deploying lambdas, and 2) creating the lambda and adding functionality in the client to call it. You will need to make similar changes to your milestone 2 code so the steps I followed are described below. Most of these can be done with the rafactoring tools available in Android Studio and other IDEs.

* Added 'server' and 'shared' modules to the project (with module type 'Java or Kotlin Library') and made the 'app' and 'server' modules both depend on the 'shared' module.
* Changed all the modules to Java version 8 (in the build.gradle files).
* Moved the 'domain', 'request', and 'response' packages to the shared module. (simple drag and drop).
* Added 'client' to the end of the base package name for the classes remaining in the 'app' project.
* Converted the 'FollowingService' classes to a proxy, with the proxy class in the client module and the interface in the shared module.  Added an implementation class to the server module.
* Created a 'FollowingDAO' class in the server module and moved the code for generating the dummy data into the DAO. Moved the tests on the generation of the dummy data into the server module.
* Added the 'shadow' plugin to Gradle to support the creation of a server "uber" jar that contains all the server code and it's dependencies (including the code from the shared module) for deployment of the lambdas. This required additions to two build.gradle files (the top-level build.gradle for the project and the one in the server module).
* Added the GetFollowing lambda to the server module.
* Created the ClientCommunicator and Serializer classes in the app module and added logic to the ServerFacade to call the lambda function.

### Running the Milestone 3 Sample Program
Before running the milestone 3 sample program, you will need to deploy the lambda and create an API that invokes it using API Gateway. Refer to the instructions from the in-class labs as needed.

**Notes:**

* To deploy the lambda, you will first need to generate a jar from the server module that contains all dependencies and use it to deploy the lambda.
  * Generate the jar from the Gradle panel on the right side of Android Studio. Open server, tasks, shadow and then double-click 'shadowJar'. If you don't see the shadowJar file in your project, you need to make the 'shadow' additions you see in the top level and server module build.gradle files of the sample code. The jar will be called 'server-all.jar' and will be in the build/libs directory of the server module.
* The project expects to find an API Endpoint (resource) named '/getfollowing' with a post method. Create and set the following request and response models for the post method of '/getfollowing' when you setup your API Gateway:

Request Model

```
{
  "definitions": {
    "user": {
      "type": "object",
      "properties": {
          "firstName": { "type": "string" },
          "lastName":  { "type": "string" },
          "alias":     { "type": "string" },
          "imageUrl":  { "type": "string" }
      },
      "required": ["firstName", "lastName", "alias"]
    }
  },
  
  "type": "object",
  "properties": {
    "follower":     { "$ref": "#/definitions/user" },
    "limit" :       { "type": "integer" },
    "lastFollowee": { "$ref": "#/definitions/user" }
  },
  "required": ["follower", "limit"]
}
```

Response Model

```
{
  "definitions": {
    "user": {
      "type": "object",
      "properties": {
          "firstName": { "type": "string" },
          "lastName":  { "type": "string" },
          "alias":     { "type": "string" },
          "imageUrl":  { "type": "string" }
      },
      "required": ["firstName", "lastName", "alias"]
    }
  },
  
  "type": "object",
  "properties": {
    "success":   { "type": "boolean" },
    "followees": { 
        "type": "array",
        "items": { "$ref": "#/definitions/user" }
    },
    "hasMorePages": { "type": "boolean" },
    "message": { "type": "string" }
  },
  "required": ["success", "followees", "hasMorePages"]
}
```

* Before you can run the Android app from the sample code you will need to update the 'SERVER_URL' variable in the ServerFacade class to the Invoke URL of the API you created above. This will be the same base URL for all of your API endpoints. Find it by navigating to your API in AWS, clicking on stages in the right-side menu, and clicking on the stage to which you deployed your API.
* The sample code invokes the API Gateway using the ClientCommunicator class in the 'app' module. It is also possible to generate an SDK from API Gateway for accessing an API. However, the generated code requires modifications before it will compile and it has several other issues. I found it much easier to invoke the API manually (by writing the ClientCommunicator class) than by using a generated SDK. However, you can use a generated SDK if you prefer.