package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.AuthToken;

public class AuthTokenDAO {

    public AuthToken generateAuthToken(String userAlias){
        //TODO: Make this generate not a hardcoded AuthToken and stick it in a DB
        return new AuthToken("myAuthToken");
    }

    public boolean validateAuthToken(AuthToken authToken){
        //TODO: check to see if it's already been too long, and you should invalidate the authToken by checking DB
        if(authToken.getAuthTokenString().equals("myAuthToken")){
            return true;
        }
        return false;
    }

    public void invalidateAuthToken(AuthToken authToken){
        //TODO: remove authToken from DB
    }
}
