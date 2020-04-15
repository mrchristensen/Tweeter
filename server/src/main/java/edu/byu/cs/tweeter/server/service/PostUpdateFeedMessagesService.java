package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.request.UpdateFeedsRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;

public class PostUpdateFeedMessagesService {
    private final String QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/QUEUE_URL_API/UpdateFeedQueue";

    public void postUpdateFeedMessages(Status status){
        System.out.println("Status received: " + status);

        FollowersServiceImpl followersService = new FollowersServiceImpl();

        System.out.println("Starting to get all followers for: " + status.getDate());
        FollowersResponse response = followersService.getFollowers(new FollowersRequest(status.getUser(), 15000, null, "myAuthToken", "test"));
        List<User> followers = response.getFollowers();
        System.out.println("Finished getting users, size: " + followers.size());

        UpdateFeedsRequest updateFeedsRequest = new UpdateFeedsRequest(status);

        //todo check for null followers?
        for (User follower : followers) {
            updateFeedsRequest.addFollower(follower);

            if(updateFeedsRequest.getNumFollowers() == 25){
                System.out.println("Send of a batch of 25");
                sendMessage(Serializer.serialize(updateFeedsRequest)); //Send 25 of for a batch write
                updateFeedsRequest.setFollowers(new ArrayList<>()); //Reset the UpdateFeedQuest
            }
        }
        if(updateFeedsRequest.getNumFollowers() > 0) {
            System.out.println("Send of a batch of: " + updateFeedsRequest.getNumFollowers() + ", finishing off the last ones");
            sendMessage(Serializer.serialize(updateFeedsRequest)); //Send the remaining feeds to update (after finishing the loop)
        }
        System.out.println("Finished sending the update feed messages");
    }

    private void sendMessage(String messageBody) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(QUEUE_URL)
                .withMessageBody(messageBody)
                .withDelaySeconds(5);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);
    }

}
