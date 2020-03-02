package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class GetRegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private final RegisterPresenter presenter;
    private final GetRegisterObserver observer;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetRegisterObserver {
        void registerRetrieved(RegisterResponse registerResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetRegisterTask(RegisterPresenter presenter, GetRegisterTask.GetRegisterObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param registerRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
        RegisterResponse response = presenter.getRegister(registerRequests[0]);
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param registerResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {

        if(observer != null) {
            observer.registerRetrieved(registerResponse);
        }
    }
}
