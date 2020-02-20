package edu.byu.cs.tweeter.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.net.request.FeedRequest}.
 */
public class FeedResponse extends PagedResponse {

    private List<Status> statuses;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FeedResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param statuses the statuses to be included in the result.
     * @param hasMorePages an indicator or whether more data is available for the request.
     */
    public FeedResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    /**
     * Returns the statuses for the corresponding request.
     *
     * @return the followees.
     */
    public List<Status> getFeed() {
        return statuses;
    }
}
