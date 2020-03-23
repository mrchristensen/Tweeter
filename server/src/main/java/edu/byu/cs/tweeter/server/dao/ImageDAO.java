package edu.byu.cs.tweeter.server.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class ImageDAO {

    public String getImage(GetImageRequest request) throws Exception {
        //TODO: Implement actual functionality once Databases are implemented (upload to AWS S3)
        String imagePath = request.getImageURL();
        System.out.println("Image path: " + imagePath);

        URL url = new URL(imagePath);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toString();
    }

}