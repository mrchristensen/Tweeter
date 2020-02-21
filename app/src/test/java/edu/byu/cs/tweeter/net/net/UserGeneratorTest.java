package edu.byu.cs.tweeter.net.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.UserGenerator;

class UserGeneratorTest {

    @Test
    void testGenerateUsers_count() {

        List<User> users = UserGenerator.getInstance().generateUsers(10);
        Assertions.assertEquals(10, users.size());

        users = UserGenerator.getInstance().generateUsers(2);
        Assertions.assertEquals(2, users.size());
    }
}