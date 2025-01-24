package com.suv.cgpt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

public class CgptClient {
    public static String DEFAULT_CGPT_URL = "https://api.openai.com/v1/chat/completions";
    public static String CGPT_URL_NAME = "cgpt.url";
    public static String CGPT_API_KEY_NAME = "cgpt.api.key";

    private Function<String, String> properties;
    private HttpClient httpClient;

    public CgptClient(){
        this.httpClient = HttpClient.newBuilder().build();
    }

    public String getProperty(String name, String defValue){
        String result = getProperties().apply(name);
        if (result == null && defValue == null) throw new NullPointerException("No property " + name);
        return result == null ? defValue : result;
    }
    public String getProperty(String name) {
        return getProperty(name, null);
    }

    public String getUrl() {
        return getProperty(CGPT_URL_NAME, DEFAULT_CGPT_URL);
    }

    public String getApiKey(){
        return this.getProperty(CGPT_API_KEY_NAME);
    }

    protected HttpRequest createSecurePostRequest(String paramsJson) {
        return this.createSecurePostRequest("", paramsJson);
    }

    protected HttpRequest createSecurePostRequest(String path, String paramsJson) {

        String address = this.getUrl() + path;

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(paramsJson);
        try {
            return HttpRequest.newBuilder().uri(new URI(address)).version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + getApiKey())
                    .POST(publisher).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Function<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Function<String, String> properties) {
        this.properties = properties;
    }

    protected String doRequest(HttpRequest request) {
        HttpClient client = this.getHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body() == null || response.body().isEmpty()) {
                throw new NullPointerException("Response empty");
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String doRequest(String msg) {
        return doRequest(createSecurePostRequest(msg));
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
