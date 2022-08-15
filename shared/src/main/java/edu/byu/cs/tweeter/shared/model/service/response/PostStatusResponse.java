package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.Status;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class PostStatusResponse {

    public Status status;

    public PostStatusResponse() {
    }

    public PostStatusResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
