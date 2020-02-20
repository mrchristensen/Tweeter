package edu.byu.cs.tweeter.presenter;

/**
 * The presenter for the main activity.
 */
public class StoryViewPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter sends notifications to it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public StoryViewPresenter(View view) {
        this.view = view;
    }
}
