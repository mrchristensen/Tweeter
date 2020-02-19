package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

/**
 * An {@link AsyncTask} for retrieving followees for a user.
 */
public class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {

    private final StoryPresenter presenter;
    private final GetStatusesObserver observer;

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
        StoryResponse response = presenter.getStory(storyRequests[0]);
//        loadImages(response); //todo: is this needed?
        return response;
    }

    /**
     * Loads the image associated with each followee included in the response.
     *
     * @param response the response from the followee request.
     */
//    private void loadImages(FollowingResponse response) {
//        for(User user : response.getFollowees()) {
//
//            Drawable drawable;
//
//            try {
//                drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
//            } catch (IOException e) {
//                Log.e(this.getClass().getName(), e.toString(), e);
//                drawable = null;
//            }
//
//            ImageCache.getInstance().cacheImage(user, drawable);
//        }
//    }

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
