package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static final String API_BASE_URL = "http://localhost:8080/users";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User[] getAllUsers() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return users;
    }

    public User getUserByUserId(int id ) {
        User user = new User();
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return user;
    }

    public User getUserByAccountId (int accountId) {
        User user = new User();
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "/account/" + accountId, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return user;
    }

    public List<Integer> getUserIdList () {
        List <Integer> userIdList = new ArrayList<>();
        User[] usersList = getAllUsers();

        for (User user: usersList) {
            userIdList.add(user.getId());
        }

        return userIdList;

    }

    public List <String> getAllUsernames () {
        User[] users = getAllUsers();
        List <String> usernames = new ArrayList<>();

        for (User user: users) {
            usernames.add(user.getUsername());
        }

        return usernames;
    }

    private HttpEntity<User> makeUserEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
