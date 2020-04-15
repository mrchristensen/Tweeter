package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.service.FollowServiceImpl;
import edu.byu.cs.tweeter.server.service.FollowersServiceImpl;
import edu.byu.cs.tweeter.server.service.PostUpdateFeedMessagesService;
import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.request.UpdateFeedsRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("PostUpdateFeedMessagesHandler received this message: " + msg.getBody());

            PostUpdateFeedMessagesService service = new PostUpdateFeedMessagesService();
            Status status = Serializer.deserialize(msg.getBody(), Status.class);

            service.postUpdateFeedMessages(status);
        }
        return null;
    }
}
