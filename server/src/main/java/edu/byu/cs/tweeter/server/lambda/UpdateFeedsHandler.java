package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.service.UpdateFeedsService;
import edu.byu.cs.tweeter.shared.json.Serializer;
import edu.byu.cs.tweeter.shared.model.service.request.UpdateFeedsRequest;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {


    @Override
    public Void handleRequest(SQSEvent event, Context context) {

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println("UpdateFeedsHandler received this message: " + msg.getBody());
            UpdateFeedsService updateFeedsService = new UpdateFeedsService();
            UpdateFeedsRequest request = Serializer.deserialize(msg.getBody(), UpdateFeedsRequest.class);

            updateFeedsService.updateFeeds(request);


            // TODO:
            // Add code to print message body to the log
//
//            FollowServiceImpl service = new FollowServiceImpl();
//            return service.removeFollow(request);
        }

        return null;
    }
}
