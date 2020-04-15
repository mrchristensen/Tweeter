package edu.byu.cs.tweeter.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import edu.byu.cs.tweeter.shared.json.Serializer;

class ClientCommunicator {
    private static final String LOG_TAG = "ClientCommunicator";

    private static final int TIMEOUT_MILLIS = 80000;

    private final String baseURL;

    ClientCommunicator(String baseURL) {
        this.baseURL = baseURL;
    }

    private interface RequestStrategy {
        void setRequestMethod(HttpURLConnection connection) throws IOException;
        void sendRequest(HttpURLConnection connection) throws IOException;
    }

    <T> T doPost(String urlPath, final Object requestInfo, Map<String, String> headers, Class<T> returnType) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy() {
            @Override
            public void setRequestMethod(HttpURLConnection connection) throws IOException {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

            }

            @Override
            public void sendRequest(HttpURLConnection connection) throws IOException {
                connection.setDoOutput(true);

                String entityBody = Serializer.serialize(requestInfo);
                try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                    os.writeBytes(entityBody);
                    os.flush();
                }
            }
        };

        return doRequest(urlPath, headers, returnType, requestStrategy);
    }

    <T> T doGet(String urlPath, Map<String, String> headers, Class<T> returnType) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy() {
            @Override
            public void setRequestMethod(HttpURLConnection connection) throws IOException {
                connection.setRequestMethod("GET");
            }

            @Override
            public void sendRequest(HttpURLConnection connection) {
                // Nothing to send. For a get, the request is sent when the connection is opened.
            }
        };

        return doRequest(urlPath, headers, returnType, requestStrategy);
    }

    <T> T doDelete(String urlPath, Map<String, String> headers, Class<T> returnType) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy() {
            @Override
            public void setRequestMethod(HttpURLConnection connection) throws IOException {
                connection.setRequestMethod("DELETE");
            }

            @Override
            public void sendRequest(HttpURLConnection connection) {
                // Nothing to send. For a delete, the request is sent when the connection is opened.
            }
        };

        return doRequest(urlPath, headers, returnType, requestStrategy);
    }

    <T> T doPut(String urlPath, Map<String, String> headers, Class<T> returnType) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy() {
            @Override
            public void setRequestMethod(HttpURLConnection connection) throws IOException {
                connection.setRequestMethod("PUT");
            }

            @Override
            public void sendRequest(HttpURLConnection connection) {
                // Nothing to send. For a delete, the request is sent when the connection is opened.
            }
        };

        return doRequest(urlPath, headers, returnType, requestStrategy);
    }

    private <T> T doRequest(String urlPath, Map<String, String> headers, Class<T> returnType, RequestStrategy requestStrategy) throws IOException {

        HttpURLConnection connection = null;

        try {
            URL url = getUrl(urlPath);

            Log.i(LOG_TAG, "url: " + url.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT_MILLIS);
            requestStrategy.setRequestMethod(connection);

            Log.i(LOG_TAG, "Request method: " + connection.getRequestMethod());

            if(headers != null) {
                for (String headerKey : headers.keySet()) {
                    connection.setRequestProperty(headerKey, headers.get(headerKey));
                }
            }

            requestStrategy.sendRequest(connection);

            Log.i(LOG_TAG, "Response code: " + connection.getResponseCode());
            Log.i(LOG_TAG, "Response message: " + connection.getResponseMessage());

            String response = getResponse(connection);
            Log.i(LOG_TAG, "Response: " + response);
            return Serializer.deserialize(response, returnType);
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private URL getUrl(String urlPath) throws MalformedURLException {
        String urlString = baseURL + (urlPath.startsWith("/") ? "" : "/") + urlPath;
        return new URL(urlString);
    }

    private String getResponse(HttpURLConnection connection) throws IOException {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }
}
