package com.synchrony.app.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.synchrony.app.config.ImgurConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ImgurService {

    // Inject the Imgur configuration using Spring's dependency injection mechanism
    @Autowired
    private ImgurConfig imgurConfig;

    // Upload one or more images to Imgur and return their URLs
    public List<String> uploadImage(List<MultipartFile> images) throws IOException {
        // Set the headers for the HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(imgurConfig.getAuthToken());

        // Create the body of the HTTP request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile image : images) {
            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            body.add("image", resource);
        }

        // Create the HTTP request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the HTTP request to the Imgur API
        ResponseEntity<String> responseEntity = imgurConfig.restTemplate().exchange(
                imgurConfig.getApiurl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Parse the response from the Imgur API
        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        // Extract the URLs of the uploaded images from the response and add them to a list
        List<String> urls = new ArrayList<>();
        urls.add(data.get("link").getAsString());
        return urls;
    }

    // Delete an image from Imgur given its ID
    public String deleteImage(String imageId) {
        // Set the headers for the HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(imgurConfig.getAuthToken());

        // Create the HTTP request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Construct the URL for the Imgur API endpoint that deletes the image
        String url = imgurConfig.getApiurl() + imageId;

        // Send the HTTP request to the Imgur API
        ResponseEntity<String> responseEntity = imgurConfig.restTemplate().exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        // Return the response from the Imgur API
        return responseEntity.getBody();
    }
}
