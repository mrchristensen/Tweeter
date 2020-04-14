package edu.byu.cs.tweeter.view.ui.main.mainActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.SessionCache;
import edu.byu.cs.tweeter.presenter.FindUserPresenter;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.view.asyncTasks.DoLogoutTask;
import edu.byu.cs.tweeter.view.asyncTasks.FindUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.ui.main.storyView.StoryViewActivity;
import edu.byu.cs.tweeter.view.ui.start.startActivity.StartActivity;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver,
        FindUserTask.FindUserObserver, MainPresenter.View, View.OnCreateContextMenuListener,
        FindUserPresenter.View, DoLogoutTask.LogoutObserver, LogoutPresenter.View,
        PostStatusPresenter.View, PostStatusTask.PostStatusObserver {
    private static final String LOG_TAG = "MainActivity";

    private MainPresenter presenter;
    private FindUserPresenter findUserPresenter;
    private LogoutPresenter logoutPresenter;
    private PostStatusPresenter postStatusPresenter;
    private User user;
    private ImageView userImageView;
    private View storyView;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this);
        findUserPresenter = new FindUserPresenter(this);
        logoutPresenter = new LogoutPresenter(this);
        postStatusPresenter = new PostStatusPresenter(this);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewStatus();
            }
        });

        user = presenter.getCurrentUser();
        userImageView = findViewById(R.id.userImage);

        // Asynchronously load the user's image
        GetImageRequest request = new GetImageRequest(user.getImageUrl());
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(request);

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText("@" + user.getAlias());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchMenuItem:
                search();
                return true;
            case R.id.logoutMenuItem:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Status");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String statusMessage = input.getText().toString();
                View view = findViewById(android.R.id.content);
                Log.i(LOG_TAG, "New status post: " + statusMessage);

                postStatus(statusMessage);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @SuppressLint("NewApi")
    private void postStatus(String statusMessage) {
        PostStatusTask postStatusTask = new PostStatusTask(postStatusPresenter,this);

        PostStatusRequest request = new PostStatusRequest(new Status(presenter.getCurrentUser(),
                LocalDateTime.now().toString(), statusMessage), SessionCache.getInstance().getAuthTokenString(), SessionCache.getInstance().getCurrentUser().getAlias());
        postStatusTask.execute(request);
    }

    private void search() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String searchInput = input.getText().toString();
                View view = findViewById(android.R.id.content);
                Log.i(LOG_TAG, "Searched for: " + searchInput);

                startStoryViewActivity(view, searchInput);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void logout() {
        DoLogoutTask doLogoutTask = new DoLogoutTask(logoutPresenter, this);

        LogoutRequest request = new LogoutRequest(presenter.getCurrentUser().getAlias(), SessionCache.getInstance().getAuthTokenString());
        doLogoutTask.execute(request);
    }

    public void startStoryViewActivity(View view, String userAlias){
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
    public void getUser(FindUserResponse response) {
        if(response.isSuccessful()){
            User user = response.getUser();
            Intent storyViewActivityIntent = new Intent(storyView.getContext(), StoryViewActivity.class);
            storyViewActivityIntent.putExtra("user", user);
            storyViewActivityIntent.putExtra("activity", "storyViewActivity");
            startActivity(storyViewActivityIntent);
        }
        else {
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "The user: @" + response.getUserAlias() + ", does not exit.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void logoutRetrieved(LogoutResponse response) {
        if(response.logoutSuccessful()){
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Logout was unsuccessful",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void statusPosted(PostStatusResponse response) {
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, "Status was successfully published: " + response.getStatus().getMessageBody(),
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}