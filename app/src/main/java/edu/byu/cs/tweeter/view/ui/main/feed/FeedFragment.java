package edu.byu.cs.tweeter.view.ui.main.feed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FindUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.ui.main.mainActivity.MainActivity;
import edu.byu.cs.tweeter.view.ui.main.storyView.StoryViewActivity;

/**
 * The fragment that displays on the 'Feed' tab.
 */
public class FeedFragment extends Fragment implements FeedPresenter.View, FindUserTask.FindUserObserver{

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FeedPresenter presenter;
    private FeedFragment fragment;

    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        presenter = new FeedPresenter(this);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(layoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedRecyclerViewPaginationScrollListener(layoutManager));

        fragment = this;

        return view;
    }

    @Override
    public void getUser(User user) {
        if(user != null){
            String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
            if(activity.equals("[null]")){
                ((MainActivity) getActivity()).startStoryViewActivity(getView(), user.getAlias());
            }
            else{
                ((StoryViewActivity) getActivity()).startStoryViewFragment(getView(), user.getAlias());
            }
        }
        else{
            Snackbar.make(getView(), "The user: \"" + user.getAlias() + "\", does not exit.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /**
     * The ViewHolder for the RecyclerView that displays the Story data.
     */
    private class FeedHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView date;
        private final TextView message;

        FeedHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.messageBody);

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        void bindStatus(Status status) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy - HH:mm");

            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(status.getUser()));
            userAlias.setText(status.getUser().getAlias());
            userName.setText(status.getUser().getName());
            date.setText(dtf.format(status.getDate()));

            CharSequence input = status.getMessageBody();
            SpannableStringBuilder builder = new SpannableStringBuilder(input);

            //Find url's - "http://..."
            Pattern pattern = Patterns.WEB_URL;
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                String text = input.subSequence(start, end).toString();

                ClickableURLSpan url = new ClickableURLSpan(text);
                builder.setSpan(url, start, end, 0);
            }

            //Find mentions - "@"
            pattern = Pattern.compile("\\B@\\w+");
            matcher = pattern.matcher(input);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                String text = input.subSequence(start, end).toString();

                ClickableMentionSpan url = new ClickableMentionSpan(text);
                builder.setSpan(url, start, end, 0);
            }

            message.setText(builder);
            message.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public class ClickableURLSpan extends URLSpan {
        ClickableURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
            String clickedURL = getURL();

            if (!clickedURL.startsWith("http://") && !clickedURL.startsWith("https://")){
                clickedURL = "http://" + clickedURL;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedURL));
            startActivity(browserIntent);
        }
    }

    public class ClickableMentionSpan extends URLSpan {
        ClickableMentionSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
            String clickedMention = getURL();

            FindUserTask findUserTask = new FindUserTask(fragment);
            findUserTask.execute(clickedMention);
        }
    }


    /**
     * The adapter for the RecyclerView that displays the Story data.
     */
    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> implements GetFeedTask.GetStatusesObserver {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of story data.
         */
        FeedRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newStatuses the users to add.
         */
        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param status the user to add.
         */
        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param status the user to remove.
         */
        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
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
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedHolder(view);
        }

        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param feedHolder the ViewHolder to which the followee should be bound.
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if(!isLoading) {
                feedHolder.bindStatus(statuses.get(position));
            }
        }

        /**
         * Returns the current number of followees available for display.
         * @return the number of followees available for display.
         */
        @Override
        public int getItemCount() {
            return statuses.size();
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
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFeedTask getFeedTask = new GetFeedTask(presenter, this);

            String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
            User user;
            if(!activity.equals("[null]")){
                user = (User) ((StoryViewActivity)getActivity()).getIntent().getSerializableExtra("user");
            }
            else{
                user = presenter.getCurrentUser();
            }
            FeedRequest request = new FeedRequest(user, PAGE_SIZE, lastStatus);
            getFeedTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param feedResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void feedRetrieved(FeedResponse feedResponse) {
            List<Status> statuses = feedResponse.getFeed();

            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = feedResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            feedRecyclerViewAdapter.addItems(statuses);
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        @SuppressLint("NewApi")
        private void addLoadingFooter() {
            addItem(new Status(new User("firstN", "lastN", "ImageURL"), LocalDateTime.now(), "Dummy status"));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class FeedRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        FeedRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
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

            if (!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
