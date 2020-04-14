package edu.byu.cs.tweeter.server.service;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;

import javax.imageio.ImageIO;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.generateAuthTokenString;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {
    private final String BUCKET_NAME = "BUCKET_NAME";

    @Override
    public RegisterResponse doRegister(RegisterRequest request) {
        UserDAO userDAO = new UserDAO();

        //Hash
        request.setPassword(HashingService.hash(request.getPassword()));

        if(userDAO.getUser(request.getAlias()) == null){ //The username isn't taken already
            String s3PictureURl = s3Upload(request.getProfileImageURL());
            request.setProfileImageURL(s3PictureURl);
            //todo: upload the picture to S3

            boolean putUser = userDAO.putUser(request.getAlias(), request.getPassword(),
                    request.getFistName(), request.getLastName(), request.getProfileImageURL());

            if(putUser){ //We successfully put the user into the database
                //AuthToken stuff
                String authTokenString = generateAuthTokenString();
                AuthTokenDAO authTokenDAO = new AuthTokenDAO();
                authTokenDAO.putAuthToken(request.getAlias(), authTokenString, ZonedDateTime.now().toString());

                return new RegisterResponse(true, new User(request.getFistName(), request.getLastName(), request.getAlias(), request.getProfileImageURL()), authTokenString);
            }
        }

        return new RegisterResponse(false, null, null);
    }

    private String s3Upload(String profileImageURL) {
        BufferedImage image = null;
        try {
            URL url = new URL(profileImageURL);
            image = ImageIO.read(url);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            byte[] buffer = os.toByteArray();
            InputStream is = new ByteArrayInputStream(buffer);
            AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withRegion("us-west-2")
                    .build();

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(buffer.length);
            meta.setContentType("image/png");

            String key = url.getPath().replaceAll("/", "").replaceAll(".png", "").replaceAll(".jpg", "");
            System.out.println("key: " + key);

            PutObjectResult test = s3.putObject(new PutObjectRequest(BUCKET_NAME, key, is, meta));
            System.out.println("Results: " + test.toString());

            System.out.println("https://" + BUCKET_NAME + ".s3.us-west-2.amazonaws.com/" + key);
            return "https://" + BUCKET_NAME + ".s3.us-west-2.amazonaws.com/" + key;





        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
