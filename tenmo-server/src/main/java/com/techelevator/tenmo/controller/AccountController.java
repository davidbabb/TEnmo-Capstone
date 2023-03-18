package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.RemoteEndpoint;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping ("/accounts")
public class AccountController {

    private static AccountDao dao;
    private static UserDao userDao;

    public AccountController() {
        userDao = new JdbcUserDao(DataSource());
        dao = new JdbcAccountDao(DataSource());
    }

    @RequestMapping (path = "/{account_id}", method = RequestMethod.GET)
    public Account GetAccountByAccountId(@PathVariable("account_id") int accountId) {
        return dao.GetAccountByAccountId(accountId);
    }

    @RequestMapping (path = "user/{user_id}", method = RequestMethod.GET)
    public Account GetAccountByUserId(@PathVariable("user_id") int userId) {
        return dao.GetAccountByUser(userId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> ListAccounts() {
        return dao.ListAccounts();
    }

    private BasicDataSource DataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }


}
