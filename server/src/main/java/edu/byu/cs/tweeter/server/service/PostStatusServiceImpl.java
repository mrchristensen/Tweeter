package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.service.PostStatusService;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class PostStatusServiceImpl implements PostStatusService {
    private final String QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/QUEUE_URL_API/PostStatusQueue";

    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        System.out.println("Authtoken String: " + request.getAuthTokenString());


        String messageBody = Serializer.serialize(request.getStatus());

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(QUEUE_URL)
                .withMessageBody(messageBody)
                .withDelaySeconds(5);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);



        if(request.getStatus() == null){
            System.out.println("Request status is null");
        }
        System.out.println("Status Message: " + request.getStatus().getMessageBody());

        if(validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())){
            StoryDAO storyDAO = new StoryDAO();
            storyDAO.putStatus(request.getStatus());

            return new PostStatusResponse(request.getStatus());
        }

        else{
            throw new RuntimeException("forbidden");
        }
    }
}
