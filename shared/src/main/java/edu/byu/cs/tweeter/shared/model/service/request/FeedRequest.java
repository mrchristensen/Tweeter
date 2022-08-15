package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user (their story).
 */
public class FeedRequest extends AuthorizedRequest {

    public User user;
    public int limit;
    public Status lastStatus;

    public FeedRequest() {
    }

    /**
     * Creates an instance.
     *
     * @param user the {@link User} whose statuses are to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last status that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public FeedRequest(User user, int limit, Status lastStatus, String authTokenString, String currentUserAlias) {
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
        this.authTokenString = authTokenString;
        this.currentUserAlias = currentUserAlias;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the last status that was returned in the previous request or null if there was no
     * previous request or if no statuses were returned in the previous request.
     *
     * @return the last status.
     */
    public Status getLastStatus() {
        return lastStatus;
    }
}
