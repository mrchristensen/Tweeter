package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages
 * of the Main Activity.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String LOG_TAG = "SectionsPagerAdapter";

    private static final int FEED_FRAGMENT_POSITION = 0;
    private static final int STORY_FRAGMENT_POSITION = 1;
    private static final int FOLLOWING_FRAGMENT_POSITION = 2;
    private static final int FOLLOWERS_FRAGMENT_POSITION = 3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case FEED_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Feed");
                return PlaceholderFragment.newInstance(position + 1); //TODO: Make this return the right fragment (not the placeholder)
            case STORY_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Story");
                return PlaceholderFragment.newInstance(position + 1); //TODO: Make this return the right fragment (not the placeholder)
            case FOLLOWING_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Following");
                return new FollowingFragment();
            case FOLLOWERS_FRAGMENT_POSITION:
                Log.i(LOG_TAG, "Clicked pos: " + position + " - Created new Followers");
                return new FollowersFragment();
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
        // Show 4 total pages.
        return 4;
    }
}