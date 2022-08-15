package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.PostStatusServiceImpl;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {

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
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        PostStatusServiceImpl service = new PostStatusServiceImpl();
        return service.postStatus(request);
    }
}
