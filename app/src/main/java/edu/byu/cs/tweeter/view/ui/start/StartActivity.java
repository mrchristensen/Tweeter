package edu.byu.cs.tweeter.view.ui.start;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.ui.main.MainActivity;

/**
 * The start activity for the application. Contains tabs for sign in and register.
 */
public class StartActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View {
//todo clean up this files comments

//    private MainPresenter presenter;
//    private User user;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        presenter = new MainPresenter(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



//        user = presenter.getCurrentUser();
//        logoImageView = findViewById(R.id.logoImage);
//        logoImageView.setImageDrawable(R.drawable.tweeter_logo);
        // Asynchronously load the logo image
//        LoadImageTask loadImageTask = new LoadImageTask(this);
//        loadImageTask.execute(presenter.getCurrentUser().getImageUrl());

//        TextView userName = findViewById(R.id.userName);
//        userName.setText(user.getName());

//        TextView userAlias = findViewById(R.id.userAlias);
//        userAlias.setText(user.getAlias());
    }

    public void startMainActivity(View view, User currentUser){
        Intent mainActivityIntent = new Intent(view.getContext(), MainActivity.class);

        mainActivityIntent.putExtra("user", currentUser);
        startActivity(mainActivityIntent);
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
//        ImageCache.getInstance().cacheImage(user, drawables[0]);
//
//        if(drawables[0] != null) {
//            userImageView.setImageDrawable(drawables[0]);
//        }
    }
}