package edu.byu.cs.tweeter.shared.model.service.request;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user (their story).
 */
public class UpdateFeedsRequest {
    public Status status;
    List<User> followers;

    public UpdateFeedsRequest() {
    }

    public UpdateFeedsRequest(Status status, List<User> followers) {
        this.status = status;
        this.followers = followers;
    }

    public UpdateFeedsRequest(Status status) {
        this.status = status;
        this.followers = new ArrayList<>();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public int getNumFollowers() {
        return followers.size();
    }
}
