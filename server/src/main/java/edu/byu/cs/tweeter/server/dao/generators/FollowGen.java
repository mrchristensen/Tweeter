package edu.byu.cs.tweeter.server.dao.generators;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowGen {
    public static void main(String[] args) {
        FollowDAO followDAO = new FollowDAO();
        List<String> userAliases = new ArrayList<>();

        int start = 0;
        int end = 10001;

        for(int i = start; i <= end; i ++) {
            userAliases.add("user" + i);
            if(userAliases.size() == 25){
                System.out.println("Send next batch of 25 - i = " + i);
                followDAO.followBatchWrite(userAliases); //Send 25 of for a batch write
                userAliases = new ArrayList<>();
            }
        }
        if(userAliases.size() > 0) {
            System.out.println("Send next batch of: " + userAliases.size() + ", to finish things off");
            followDAO.followBatchWrite(userAliases); //Send the remaining feeds to update (after finishing the loop)
        }
        System.out.println("Finished making users from " + start + " to " + end);
    }
}
