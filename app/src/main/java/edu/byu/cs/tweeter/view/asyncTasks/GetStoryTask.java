package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

/**
 * An {@link AsyncTask} for retrieving followees for a user.
 */
public class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {

    private final StoryPresenter presenter;
    private final GetStatusesObserver observer;

    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetStatusesObserver {
        void storyRetrieved(StoryResponse storyResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followees.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetStoryTask(StoryPresenter presenter, GetStatusesObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followees.
     *
     * @param storyRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected StoryResponse doInBackground(StoryRequest... storyRequests) {
        StoryResponse response = null;
        try {
            response = presenter.getStory(storyRequests[0]);
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param storyResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(StoryResponse storyResponse) {

        if(observer != null) {
            observer.storyRetrieved(storyResponse);
        }
    }
}
