package edu.byu.cs.tweeter.shared.model.service.response;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class GetImageResponse {

   public String imageBytes;

    public GetImageResponse() {
    }

    public GetImageResponse(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }
}
