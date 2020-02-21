package edu.byu.cs.tweeter.net;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A temporary class that generates and returns {@link User} objects. This class may be removed when
 * the server is created and the ServerFacade no longer needs to return dummy data.
 */
public class StatusGenerator {
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
    @SuppressLint("NewApi")
    public List<Status> generateStatuses(int minFollowersPerUser, int maxFollowersPerUser, User user) {
        Random random = new Random();
        int count = random.nextInt(maxFollowersPerUser - minFollowersPerUser) + minFollowersPerUser;

        List<Status> statuses = new ArrayList<>(count);

        while(statuses.size() < count) {
            // Generate a generic status
            statuses.add(new Status(user, getRandomDateTime(), "This is a very generic tweet. This is a very generic tweet. This is a very generic tweet. This is a very generic tweet. This is a very generic tweet. This is a very generic tweet."));
        }

        return statuses;
    }

    @SuppressLint("NewApi")
    private LocalDateTime getRandomDateTime(){
        Random random = new Random();

        long minDay = LocalDate.of(2006, 3, 1).toEpochDay();
        long maxDay = LocalDate.of(2020, 2, 18).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        System.out.println(randomDate);

        return LocalDateTime.of(randomDate, LocalTime.of(random.nextInt(24), random.nextInt(60),
                random.nextInt(60), random.nextInt(999999999 + 1)));
    }
}
