package edu.byu.cs.tweeter.shared.model.service.request;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user (their story).
 */
public class UpdateFeedsRequest {
    public Status status;
    List<String> followerAliases;

    public UpdateFeedsRequest() {
    }

    public UpdateFeedsRequest(Status status, List<String> followerAliases) {
        this.status = status;
        this.followerAliases = followerAliases;
    }

    public UpdateFeedsRequest(Status status) {
        this.status = status;
        this.followerAliases = new ArrayList<>();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFollowerAliases() {
        return followerAliases;
    }

    public void setFollowerAliases(List<String> followerAliases) {
        this.followerAliases = followerAliases;
    }

    public void addFollower(String followerAlias) {
        followerAliases.add(followerAlias);
    }

    public int getNumFollowers() {
        return followerAliases.size();
    }
}
