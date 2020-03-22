package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.presenter.PostStatusPresenter;
import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class PostStatusTask extends AsyncTask<PostStatusRequest, Void, PostStatusResponse> {

    private final PostStatusPresenter presenter;
    private final PostStatusObserver observer;

    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface PostStatusObserver {
        void statusPosted(PostStatusResponse response);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public PostStatusTask(PostStatusPresenter presenter, PostStatusObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param requests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected PostStatusResponse doInBackground(PostStatusRequest... requests) {
        PostStatusResponse response = null;
        try {
            response = presenter.postStatus(requests[0]);
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param response the response that was received by the task.
     */
    @Override
    protected void onPostExecute(PostStatusResponse response) {
        //todo
        if(observer != null) {
            observer.statusPosted(response);
        }
    }
}
