package edu.byu.cs.tweeter.net;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A temporary class that generates and returns {@link User} objects. This class may be removed when
 * the server is created and the ServerFacade no longer needs to return dummy data.
 */
public class StatusGenerator {

    private static int index = 1;

    private static StatusGenerator instance;

    /**
     * A private constructor that ensures no instances of this class can be created.
     */
    private StatusGenerator() {}

    /**
     * Returns the singleton instance of the class
     *
     * @return the instance.
     */
    public static StatusGenerator getInstance() {
        if(instance == null) {
            instance = new StatusGenerator();
        }

        return instance;
    }

    /**
     * Generates the specified number of status, with a global index
     *
     * @param minFollowersPerUser the min number of statuses to generate.
     * @param maxFollowersPerUser the min number of statuses to generate.
     * @param user The owner of the statuses.
     * @return the generated users.
     */
    public List<Status> generateStatuses(int minFollowersPerUser, int maxFollowersPerUser, User user) {
        Random random = new Random();
        int count = random.nextInt(maxFollowersPerUser - minFollowersPerUser) + minFollowersPerUser;

        List<Status> statuses = new ArrayList<>(count);

        while(statuses.size() < count) {
            // Generate a generic status
            statuses.add(new Status(user, "This is a generic tweet, number " + index));
            index++;
        }

        return statuses;
    }
}
