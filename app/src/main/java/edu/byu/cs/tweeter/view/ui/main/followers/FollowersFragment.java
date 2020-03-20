package edu.byu.cs.tweeter.view.ui.main.followers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowersTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.ui.main.mainActivity.MainActivity;
import edu.byu.cs.tweeter.view.ui.main.storyView.StoryViewActivity;

/**
 * The fragment that displays on the 'Following' tab.
 */
public class FollowersFragment extends Fragment implements FollowersPresenter.View {

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FollowersPresenter presenter;

    private FollowerRecyclerViewAdapter followerRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        presenter = new FollowersPresenter(this);

        RecyclerView followerRecyclerView = view.findViewById(R.id.followerRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        followerRecyclerView.setLayoutManager(layoutManager);

        followerRecyclerViewAdapter = new FollowerRecyclerViewAdapter();
        followerRecyclerView.setAdapter(followerRecyclerViewAdapter);

        followerRecyclerView.addOnScrollListener(new FollowRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    /**
     * The ViewHolder for the RecyclerView that displays the Following data.
     */
    private class FollowerHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        FollowerHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
                    if(activity.equals("[null]")){
                        ((MainActivity) getActivity()).startStoryViewActivity(view, userAlias.getText().toString());
                    }
                    else{
                        ((StoryViewActivity) getActivity()).startStoryViewFragment(view, userAlias.getText().toString());
                    }
                }
            });
        }

        void bindUser(User user) {
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(user));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
        }
    }

    /**
     * The adapter for the RecyclerView that displays the Following data.
     */
    private class FollowerRecyclerViewAdapter extends RecyclerView.Adapter<FollowerHolder> implements GetFollowersTask.GetFollowersObserver {

        private final List<User> users = new ArrayList<>();

        private User lastFollower;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of following data.
         */
        FollowerRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newUsers the users to add.
         */
        void addItems(List<User> newUsers) {
            int startInsertPosition = users.size();
            users.addAll(newUsers);
            this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param user the user to add.
         */
        void addItem(User user) {
            users.add(user);
            this.notifyItemInserted(users.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param user the user to remove.
         */
        void removeItem(User user) {
            int position = users.indexOf(user);
            users.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         *  Creates a view holder for a followee to be displayed in the RecyclerView or for a message
         *  indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public FollowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowerHolder(view);
        }

        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param followingHolder the ViewHolder to which the followee should be bound.
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull FollowerHolder followingHolder, int position) {
            if(!isLoading) {
                followingHolder.bindUser(users.get(position));
            }
        }

        /**
         * Returns the current number of followees available for display.
         * @return the number of followees available for display.
         */
        @Override
        public int getItemCount() {
            return users.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFollowersTask getFollowersTask = new GetFollowersTask(presenter, this);

            String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
            User user;
            if(!activity.equals("[null]")){
                user = (User) ((StoryViewActivity)getActivity()).getIntent().getSerializableExtra("user");
            }
            else{
                user = presenter.getCurrentUser();
            }
            FollowersRequest request = new FollowersRequest(user, PAGE_SIZE, lastFollower);
            getFollowersTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param followersResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void followersRetrieved(FollowersResponse followersResponse) {
            List<User> followers = followersResponse.getFollowers();

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() -1) : null;
            hasMorePages = followersResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            followerRecyclerViewAdapter.addItems(followers);
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new User("Dummy", "User", ""));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(users.get(users.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FollowRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FollowRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!followerRecyclerViewAdapter.isLoading && followerRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    followerRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
