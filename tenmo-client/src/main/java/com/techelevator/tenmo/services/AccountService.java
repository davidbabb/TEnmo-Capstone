package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import okhttp3.Response;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static final String API_BASE_URL = "http://localhost:8080/accounts";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    private String authToken = null;

    public AccountService(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public AccountService () {}

    public void setAuthToken (String authToken) {
        this.authToken = authToken;
    }

    public Account getAccountByAccountId(int id) {
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return account;
    }

    public Account getAccountsByUserId(int id) {
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/user/" + id, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return account;
    }

    public BigDecimal getBalanceByUserId(int id) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        Account account = getAccountsByUserId(id);

        return account.getBalance();
    }

    public BigDecimal getBalanceByAccountId(int id) {
        Account account = getAccountByAccountId(id);
        return account.getBalance();
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
