package edu.byu.cs.tweeter.shared.model.service.response;

import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class FollowersResponse extends PagedResponse {

    public List<User> followers;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowersResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param followers the followers to be included in the result.
     * @param hasMorePages an indicator or whether more data is available for the request.
     */
    public FollowersResponse(List<User> followers, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followers = followers;
    }

    /**
     * Returns the followers for the corresponding request.
     *
     * @return the followers.
     */
    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
}
