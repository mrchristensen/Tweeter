package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.response.GetImageResponse;

public class GetImageHandlerTest {

    private GetImageHandler handler;
    GetImageRequest request;
    GetImageResponse response;

    @BeforeEach
    void setup() {
        handler = new GetImageHandler();
        request = null;
        response = null;
    }

    @Test
    void validGetImage() {
        request = new GetImageRequest("https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        response = handler.handleRequest(request, null);

        assert(response.getImageBytes().contains("PNG"));
        assert(request.getImageURL().equals("https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U"));
    }

    @Test
    void invalidGetImageURL() {
        request = new GetImageRequest("https://INVALIDURL.com/dafny.png");

        try {
            response = handler.handleRequest(request, null);
        }
        catch (Exception e){
            assert(e.getMessage().equals("server error"));
        }

        assert(response == null);
    }

}
