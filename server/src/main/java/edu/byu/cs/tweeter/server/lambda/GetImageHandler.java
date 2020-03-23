package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.service.GetImageServiceImpl;
import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.response.GetImageResponse;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetImageHandler implements RequestHandler<GetImageRequest, GetImageResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public GetImageResponse handleRequest(GetImageRequest request, Context context) {
        GetImageServiceImpl service = new GetImageServiceImpl();
        return service.getImage(request);
    }
}
