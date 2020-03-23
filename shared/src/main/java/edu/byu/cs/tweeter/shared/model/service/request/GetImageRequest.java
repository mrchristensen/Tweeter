package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the service return the next page of
 * followees for a specified follower.
 */
public class GetImageRequest {

    public String imageURL;

    public GetImageRequest() {
    }

    public GetImageRequest(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}