package edu.byu.cs.tweeter.view.ui.main.story;

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
import edu.byu.cs.tweeter.net.SessionCache;
import edu.byu.cs.tweeter.presenter.FindUserPresenter;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FindUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.ui.main.mainActivity.MainActivity;
import edu.byu.cs.tweeter.view.ui.main.storyView.StoryViewActivity;

/**
 * The fragment that displays on the 'Story' tab.
 */
public class StoryFragment extends Fragment implements StoryPresenter.View, FindUserTask.FindUserObserver, FindUserPresenter.View {

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private StoryPresenter presenter;
    private StoryFragment fragment;
    private FindUserPresenter findUserPresenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        presenter = new StoryPresenter(this);
        findUserPresenter = new FindUserPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

        fragment = this;

        return view;
    }

    /**
     * The ViewHolder for the RecyclerView that displays the Story data.
     */
    private class StoryHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView date;
        private final TextView message;

        StoryHolder(@NonNull final View itemView) {
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
            date.setText(status.getDate());

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

            FindUserTask findUserTask = new FindUserTask(findUserPresenter ,fragment);

            FindUserRequest request = new FindUserRequest(clickedMention);
            findUserTask.execute(request);
        }
    }

    /**
     * The adapter for the RecyclerView that displays the Story data.
     */
    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements GetStoryTask.GetStatusesObserver {

        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of story data.
         */
        StoryRecyclerViewAdapter() {
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
        public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryHolder(view);
        }

        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param storyHolder the ViewHolder to which the followee should be bound.
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull StoryHolder storyHolder, int position) {
            if(!isLoading) {
                storyHolder.bindStatus(statuses.get(position));
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

            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);

            String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
            User user;
            if(!activity.equals("[null]")){
                user = (User) getActivity().getIntent().getSerializableExtra("user");
            }
            else{
                user = presenter.getCurrentUser();
            }
            StoryRequest request = new StoryRequest(user, PAGE_SIZE, lastStatus, SessionCache.getInstance().getAuthTokenString());
            getStoryTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param storyResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void storyRetrieved(StoryResponse storyResponse) {
            List<Status> statuses = storyResponse.getStory();

            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = storyResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statuses);
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new Status(new User("firstN", "lastN", "ImageURL"), LocalDateTime.now().toString(), "Dummy status"));
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
    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
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

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

    @Override
    public void getUser(FindUserResponse response) {
        if(response.isSuccessful()){ //Found user
            User user = response.getUser();
            String activity = Arrays.toString(new String[]{Objects.requireNonNull(getActivity()).getIntent().getStringExtra("activity")});
            if(activity.equals("[null]")){
                ((MainActivity) getActivity()).startStoryViewActivity(getView(), user.getAlias());
            }
            else{
                ((StoryViewActivity) getActivity()).startStoryViewFragment(getView(), user.getAlias());
            }
        }
        else{ //No such user exists
            Snackbar.make(getView(), "The user: " + response.getUserAlias() + " does not exit.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
