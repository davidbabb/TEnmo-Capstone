package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentialsDto;

import java.util.Map;

public class AuthenticationService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService = new UserService();

    public AuthenticationService(String url) {
        this.baseUrl = url;
    }

    public AuthenticatedUser login(UserCredentialsDto credentials) {
        HttpEntity<UserCredentialsDto> entity = createCredentialsEntity(credentials);
        AuthenticatedUser user = null;
        String token = null;
        try {
            ResponseEntity<AuthenticatedUser> response =
                    restTemplate.exchange(baseUrl + "login", HttpMethod.POST, entity, AuthenticatedUser.class);
            user = response.getBody();
            user.setToken(response.getBody().getToken().toString());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return user;
    }

    public boolean register(UserCredentialsDto credentials) {
        HttpEntity<UserCredentialsDto> entity = createCredentialsEntity(credentials);
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "register", HttpMethod.POST, entity, Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<UserCredentialsDto> createCredentialsEntity(UserCredentialsDto credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(credentials, headers);
    }

    public String makeToken (HttpEntity<UserCredentialsDto> entity) {
        String token = null;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/login", HttpMethod.GET, entity, Map.class);
            token = response.getBody().get("token").toString();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return token;
    }
}
