# Project Milestone #4: Complete back-end by connecting to persistence layer

## Performance Requirements
* The perceived latency of the create new status operation (from the perspective of the author) is to be less than 1000 milliseconds.
* When a new status is created, that status is visible in the feeds of all of the followers of the author within 120 seconds, for authors with up to 10K followers.
* Each page of a user's feed is returned in less than 1000 milliseconds, from the perspective of the user.

To meet these performance requirements you are to do the following:
* When a new status is posted, the feed of each follower is updated. (That is feeds are updated at write-time rather than assembled at read-time.)
* Use two AWS SQS queues for asynchronously processing feed updates.
* Your feed table is to be configured with no more than 100 WCUs.

## Design
The back-end for your application will run in AWS Lambda. Some of this code will be triggered from the AWS API Gateway, other code will be triggered from AWS SQS. Your design should include the following layers:

* The Lambda handler
* A services layer to which the handler delegates each request
* A DAO layer which now interacts with your DynamoDB tables

In all of these layers, look for ways to avoid duplicating code. The Template Method or Strategy pattern may help.

Your implementation is to meet the "user and session management" requirements in the [project overview](../README.md).

## DynamoDB Notes
**DynamoDB Provisioned Capacity**

* No matter what architecture you develop, your performance will be capped by the capacity you provision for writing to the feed table in DynamoDB.
* To learn about provisioned capacity for reads (RCUs), read the following: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ProvisionedThroughput.html#ItemSizeCalculations.Reads (Links to an external site.)
* To learn about provisioned capacity for writes (WCUs), read the following: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ProvisionedThroughput.html#ItemSizeCalculations.Writes (Links to an external site.)
* Batch writes will be more efficient than individual put-items, because you will have fewer network round trips. A batch-write operation is limited to 25 items. If you include 25 items, you will consume 25 write capacity units, as long as each item is no more than 1 KB in size.
* Updating the feeds when a status is posted by an author that has 10,000 followers will require writing 10,000 items, possibly in 400 batches (10000/25). To write that many items in 120 seconds will require WCU setting of around 10,000/120.

**Populating Your Database With Test Data**

For testing and passing-off, you will need to create ~10,000 test users and add data to your follows table such that you have at least one user with 10,000 or more followers.  To do this, consider writing a script using the AWS CLI or a simple program using the AWS SDK API to create users and add followers to your follows table.  The generated users need not have profile pictures, or they could all have the same profile picture (do whatever makes sense for your implementation).

When you run your script/program to populate your database, you will probably need to temporarily increase the WCU settings on your users and follows tables (e.g., to 100).  Once the test users and followers have been created, decrease the WCU settings on these tables back to their original levels (e.g., to 1).

**Minimizing AWS charges**

There may be a small charge associated with this lab, but to minimize this consider the following:
* Turn up the DynamoDB WCUs for your feed table while testing and passing-off, but turn it down otherwise to avoid getting charged for capacity you are not using.  Also, be sure to turn down the WCU settings on your users and follows tables after your test data has been generated (as previously mentioned).
* When a lambda trigger is associated with an SQS queue, AWS regularly polls the queue for new messages so it can call your lambda when new messages arrive.  All of this polling incurs AWS charges.  Therefore, when you are not actively testing or passing-off, disconnect your lambdas from your SQS queues (i.e., remove the lambda triggers in the SQS configuration).  This will avoid incurring unnecessary charges

## Rubric
[20 pts] For meeting the three performance requirements listed above, while using no more than 100 WCUs on the feed table. For full points, you must update feeds asynchronously when a status is created and must use SQS.

[20 pts] For DAOs that correctly write to DynamoDB, with profile pictures stored in S3.

[10 pts] For authentication and session management that meet requirements.

[5 pts] For database schema (based on submitted table descriptions.

[5 pts] For sequence diagram.

## Related Content

* [Some gotchas for AWS and $$$$ and tips to avoid getting charged (middle of the page)](/class-documentation/AWS%20and%20Money.md)
* [DynamoDB example code (literally copy and pasteable](dynamodb-example/)
* Maven Dependencies that are needed: ```com.amazonaws:aws-java-sdk-core:1.11.547``` and ```com.amazonaws:aws-java-sdk-dynamodb:1.11.547```
