package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.services.FollowServiceProxy;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * The presenter for the main activity.
 */
public class StoryViewPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter sends notifications to it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public StoryViewPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowResponse getFollow(FollowRequest request) throws IOException {
        FollowService service = new FollowServiceProxy();
        return service.getFollow(request);
    }

    public FollowResponse removeFollow(FollowRequest request) throws IOException {
        FollowService service = new FollowServiceProxy();
        return service.removeFollow(request);
    }

    public FollowResponse addFollow(FollowRequest request) throws IOException {
        FollowService service = new FollowServiceProxy();
        return service.addFollow(request);
    }
}
