package edu.byu.cs.tweeter.view.ui.storyView;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * The main activity for the application. Contains tabs for story, following, and followers.
 */
//Todo: Add follow button
//Todo: Remove tab for feed
public class StoryViewActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, StoryViewPresenter.View {
    private static final String LOG_TAG = "StoryViewActivity";

    private StoryViewPresenter presenter;
    private User user;
    private ImageView userImageView;

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        userImageView = findViewById(R.id.userImage);

//        user = presenter.getCurrentUser();
        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            user = (User) intent.getSerializableExtra("user");
        }
        else{
            Log.e(LOG_TAG, "User was not found when starting a StoryViewActivity!");
            Snackbar.make(userImageView, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        final Button followButton = findViewById(R.id.button_follow);
        if(new ServerFacade().userFollows(presenter.getCurrentUser(), user)){ //todo make async
            followButton.setText("Following");
        }
        else{
            followButton.setText("Follow");
        }


        followButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ServerFacade serverFacade = new ServerFacade();
                if(serverFacade.userFollows(presenter.getCurrentUser(), user)){ //todo make async
                    //Remove following relation
                    serverFacade.removeFollowing(user, presenter.getCurrentUser());
                    followButton.setText("Follow");
                }
                else{
                    //Add following relation
                    serverFacade.addFollowing(user, presenter.getCurrentUser());
                    followButton.setText("Following");
                }
                //todo
                // Code here executes on main thread after user presses button
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

    public void startStoryViewFragment(View view, String userAlias){
        Intent storyViewActivityIntent = new Intent(view.getContext(), StoryViewActivity.class);

        User user = new ServerFacade().findUser(userAlias);
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
}