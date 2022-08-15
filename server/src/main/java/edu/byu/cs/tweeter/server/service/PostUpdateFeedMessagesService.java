package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.service.request.UpdateFeedsRequest;

public class PostUpdateFeedMessagesService {
    private final String QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/QUEUE_URL_API/UpdateFeedQueue";

    public void postUpdateFeedMessages(Status status){
        System.out.println("Status received: " + status);

        FollowDAO followDAO = new FollowDAO();

        System.out.println("Starting to get all followers for: " + status.getDate());
        List<String> followerAliases = followDAO.getAllFollowerAliases(status.getUser().getAlias(), 15000);
        System.out.println("Finished getting users, size: " + followerAliases.size());

        UpdateFeedsRequest updateFeedsRequest = new UpdateFeedsRequest(status);


        System.out.println("Start sending the update feed messages");
        int i = 0;
        for (String follower : followerAliases) {
            updateFeedsRequest.addFollower(follower);

            if(updateFeedsRequest.getNumFollowers() == 25){
                i++;
                System.out.println("Send of a batch of 25, total feed updates sent: " + (i*25));
                sendMessage(Serializer.serialize(updateFeedsRequest)); //Send 25 of for a batch write
                updateFeedsRequest.setFollowerAliases(new ArrayList<>()); //Reset the UpdateFeedQuest
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
