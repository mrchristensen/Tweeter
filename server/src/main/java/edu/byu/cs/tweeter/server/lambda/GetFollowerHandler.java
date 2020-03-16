package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;
import edu.byu.cs.tweeter.server.service.FollowingServiceImpl;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowerHandler implements RequestHandler<FollowerRequest, FollowerResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers.
     */
    @Override
    public FollowerResponse handleRequest(FollowerRequest request, Context context) {
        FollowerServiceImpl service = new FollowerServiceImpl();
        return service.getFollowers(request);
    }
}
