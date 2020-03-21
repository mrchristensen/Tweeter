package edu.byu.cs.tweeter.view.ui.main.storyView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.presenter.FindUserPresenter;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.view.asyncTasks.DeleteFollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.FindUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.PutFollowTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

/**
 * The main activity for the application. Contains tabs for story, following, and followers.
 */
public class StoryViewActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, StoryViewPresenter.View, GetFollowTask.GetFollowObserver, FindUserTask.FindUserObserver,FindUserPresenter.View, DeleteFollowTask.GetFollowObserver, PutFollowTask.GetFollowObserver {
    private static final String LOG_TAG = "StoryViewActivity";

    private StoryViewActivity storyViewActivity;
    private StoryViewPresenter presenter;
    private FindUserPresenter findUserPresenter;
    private User user;
    private ImageView userImageView;
    private Button followButton;
    private boolean isFollowing;
    private View storyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        storyViewActivity = this;

        presenter = new StoryViewPresenter(this);
        findUserPresenter = new FindUserPresenter(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        userImageView = findViewById(R.id.userImage);

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            user = (User) intent.getSerializableExtra("user");
        }
        else{
            Log.e(LOG_TAG, "User was not found when starting a StoryViewActivity!");
            Snackbar.make(userImageView, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        followButton = findViewById(R.id.button_follow);
        if(presenter.getCurrentUser().equals(user)){
            followButton.setVisibility(View.GONE);
            followButton.setEnabled(false);
        }

        checkUserFollows(); //To update the button with the right text

        followButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FollowRequest request = new FollowRequest(presenter.getCurrentUser().getAlias(), user.getAlias());

                if(isFollowing){ //Remove following relation
                    DeleteFollowTask deleteFollowTask = new DeleteFollowTask(presenter, storyViewActivity);
                    deleteFollowTask.execute(request);
                }
                else{ //Add following relation
                    PutFollowTask putFollowTask = new PutFollowTask(presenter, storyViewActivity);
                    putFollowTask.execute(request);
                }
            }
        });

        // Asynchronously load the user's image
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(user.getImageUrl());

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());
    }

    private void checkUserFollows() {
        GetFollowTask getFollowTask = new GetFollowTask(presenter, this);

        FollowRequest request = new FollowRequest(presenter.getCurrentUser().getAlias(), user.getAlias());
        getFollowTask.execute(request);
    }

    public void startStoryViewFragment(View view, String userAlias){
        storyView = view;

        FindUserTask findUserTask = new FindUserTask(findUserPresenter, this);

        FindUserRequest request = new FindUserRequest(userAlias);
        findUserTask.execute(request);
    }

    @Override
    public void imageLoadProgressUpdated(Integer progress) {
        // We're just loading one image. No need to indicate progress.
    }

    /**
     * A callback that indicates that the image for the user being displayed on this activity has
     * been loaded.
     *
     * @param drawables the drawables (there will only be one).
     */
    @Override
    public void imagesLoaded(Drawable[] drawables) {
        ImageCache.getInstance().cacheImage(user, drawables[0]);

        if(drawables[0] != null) {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

    @Override
    public void followRetrieved(FollowResponse followResponse) {
        if(followResponse.isFollows()){
            followButton.setText(R.string.Following);
            isFollowing = true;
        }
        else{
            followButton.setText(R.string.Follow);
            isFollowing = false;
        }
    }

    @Override
    public void getUser(FindUserResponse response) {
        Intent storyViewActivityIntent = new Intent(storyView.getContext(), StoryViewActivity.class);
        storyViewActivityIntent.putExtra("user", user);
        storyViewActivityIntent.putExtra("activity", "storyViewActivity");
        startActivity(storyViewActivityIntent);
    }
}