package edu.byu.cs.tweeter.server.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;

public class AuthTokenService {
    private static final int AUTHTOKEN_EXPIRE_TIME = 120; //in minutes

    static boolean validateAuthToken(String userAlias, String authTokenString){
        //TODO: Remove - for testing purposes only
        if(authTokenString.equals("myAuthToken") && userAlias.equals("test")){
            System.out.println("Test validation of authToken");
            return true;
        }

        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        AuthToken authToken = authTokenDAO.getAuthToken(userAlias, authTokenString);

        if(authToken == null) { //Check to see if there is an AuthToken
            System.out.println("No such AuthToken exists");
            return false;
        }

        //Check to see if the AuthToken expired
        if(ZonedDateTime.now().isBefore(authToken.getTimeStamp().plusMinutes(AUTHTOKEN_EXPIRE_TIME))){
            System.out.println("Valid AuthToken");
            return true;
        }
        else{
            System.out.println("AuthToken has expired");
            authTokenDAO.deleteAuthToken(authTokenString); //Remove the expired AuthToken
            return false;
        }
    }

    static String generateAuthTokenString(){
        return UUID.randomUUID().toString();
    }
}
