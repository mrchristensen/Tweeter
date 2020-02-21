package edu.byu.cs.tweeter.view.ui.start;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.view.ui.PlaceholderFragment;
import edu.byu.cs.tweeter.view.ui.feed.FeedFragment;
import edu.byu.cs.tweeter.view.ui.followers.FollowersFragment;
import edu.byu.cs.tweeter.view.ui.following.FollowingFragment;
import edu.byu.cs.tweeter.view.ui.start.login.LoginFragment;
import edu.byu.cs.tweeter.view.ui.start.register.RegisterFragment;
import edu.byu.cs.tweeter.view.ui.story.StoryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages
 * of the Start Activity.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String LOG_TAG = "SectionsPagerAdapter";

    private static final int LOGIN_FRAGMENT_POSITION = 0;
    private static final int SIGNUP_FRAGMENT_POSITION = 1;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.loginTabTitle, R.string.signupTabTitle};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case LOGIN_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Login Fragment");
                return new LoginFragment();
            case SIGNUP_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Register Fragment");
                return new RegisterFragment();
            default:
                Log.e(LOG_TAG, "Clicked pos: " + position + " - where did NOT match any selection. Created new placeholder");
                return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}