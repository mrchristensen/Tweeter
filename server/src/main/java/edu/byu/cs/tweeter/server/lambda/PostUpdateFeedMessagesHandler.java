package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.service.PostUpdateFeedMessagesService;
import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.domain.Status;

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
