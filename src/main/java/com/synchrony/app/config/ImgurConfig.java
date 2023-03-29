package com.synchrony.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ImgurConfig {

    private static final Logger logger = LoggerFactory.getLogger(ImgurConfig.class);

    @Value("${imgur.client.id}")
    private String clientId;

    @Value("${imgur.client.secret}")
    private String clientSecret;
    @Value("${imgur.api.url}")
    private String apiurl;
    @Value("${imgur.auth.token}")
    private String authToken;

    @Bean
    public RestTemplate restTemplate() {
        logger.info("Creating new RestTemplate bean");
        return new RestTemplate();
    }

    public String getClientId() {
        logger.debug("Getting clientId: {}", clientId);
        return clientId;
    }

    public void setClientId(String clientId) {
        logger.debug("Setting clientId to: {}", clientId);
        this.clientId = clientId;
    }

    public String getClientSecret() {
        logger.debug("Getting clientSecret: {}", clientSecret);
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        logger.debug("Setting clientSecret to: {}", clientSecret);
        this.clientSecret = clientSecret;
    }

    public String getApiurl() {
        logger.debug("Getting apiurl: {}", apiurl);
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        logger.debug("Setting apiurl to: {}", apiurl);
        this.apiurl = apiurl;
    }

    public String getAuthToken() {
        logger.debug("Getting authToken: {}", authToken);
        return authToken;
    }

    public void setAuthToken(String authToken) {
        logger.debug("Setting authToken to: {}", authToken);
        this.authToken = authToken;
    }
}
