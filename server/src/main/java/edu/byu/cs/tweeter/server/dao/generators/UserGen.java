package edu.byu.cs.tweeter.server.dao.generators;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserGen {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        List<String> userAliases = new ArrayList<>();

        int start = 0;
        int end = 10001;

        for(int i = start; i <= end; i ++) {
            userAliases.add("user" + i);
            if(userAliases.size() == 25){
                System.out.println("Send next batch of 25 - i = " + i);
                userDAO.userBatchWrite(userAliases); //Send 25 of for a batch write
                userAliases = new ArrayList<>();
            }
        }
        if(userAliases.size() > 0) {
            System.out.println("Send next batch of: " + userAliases.size() + ", to finish things off");
            userDAO.userBatchWrite(userAliases); //Send the remaining feeds to update (after finishing the loop)
        }
        System.out.println("Finished making users from " + start + " to " + end);
    }
}
