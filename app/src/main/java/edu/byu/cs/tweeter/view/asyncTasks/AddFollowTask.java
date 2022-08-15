package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class AddFollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {

    private final StoryViewPresenter presenter;
    private final GetFollowObserver observer;

    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetFollowObserver {
        void followRetrieved(FollowResponse followResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public AddFollowTask(StoryViewPresenter presenter, GetFollowObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param followRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowResponse doInBackground(FollowRequest... followRequests) {
        FollowResponse response = null;
        try {
            response = presenter.addFollow(followRequests[0]);
        } catch (IOException e) {
            exception = e;

            e.printStackTrace();
        }
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowResponse followResponse) {
        if(observer != null) {
            observer.followRetrieved(followResponse);
        }
    }
}
