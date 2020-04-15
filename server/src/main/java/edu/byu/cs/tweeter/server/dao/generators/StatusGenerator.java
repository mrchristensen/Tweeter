package edu.byu.cs.tweeter.server.dao.generators;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A temporary class that generates and returns Follow objects. This class may be removed when the
 * server is created and the ServerFacade no longer needs to return dummy data.
 */
public class StatusGenerator {

    private static StatusGenerator statusGenerator;

    /**
     * A private constructor that ensures no instances of this class can be created from outside
     * the class.
     */
    private StatusGenerator() {}

    /**
     * Returns the singleton instance of the class
     *
     * @return the instance.
     */
    public static StatusGenerator getInstance() {
        if(statusGenerator == null) {
            statusGenerator = new StatusGenerator();
        }

        return statusGenerator;
    }

    public static List<Status> getNStatuses(int n, User user) {
        List<Status> statuses = new ArrayList<>();
        for(int i=0; i < n; i++){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy - HH:mm");
            statuses.add(new Status(user, ZonedDateTime.now().format(formatter),"Fake tweet @test @asdf" + i));
        }
        return statuses;
    }

}
