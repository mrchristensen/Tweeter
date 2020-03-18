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
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

/**
 * The main activity for the application. Contains tabs for story, following, and followers.
 */
public class StoryViewActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, StoryViewPresenter.View, GetFollowTask.GetFollowObserver {
    private static final String LOG_TAG = "StoryViewActivity";

    private StoryViewPresenter presenter;
    private User user;
    private ImageView userImageView;
    private Button followButton;
    private boolean isFollowing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        presenter = new StoryViewPresenter(this);

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
                ServerFacade serverFacade = new ServerFacade();
                if(isFollowing){
                    //Remove following relation
                    serverFacade.removeFollowing(user, presenter.getCurrentUser()); //todo make this async
                    followButton.setText(R.string.Follow);
                    isFollowing = false;
                }
                else{
                    //Add following relation
                    serverFacade.addFollowing(user, presenter.getCurrentUser()); //todo make this async
                    followButton.setText(R.string.Following);
                    isFollowing = true;
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
        Intent storyViewActivityIntent = new Intent(view.getContext(), StoryViewActivity.class);

        User user = new ServerFacade().findUser(userAlias); //todo make this async
        storyViewActivityIntent.putExtra("user", user);
        storyViewActivityIntent.putExtra("activity", "storyViewActivity");
        startActivity(storyViewActivityIntent);
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
            followButton.setText("Following");
            isFollowing = true;
        }
        else{
            followButton.setText("Follow");
            isFollowing = false;
        }
    }
}