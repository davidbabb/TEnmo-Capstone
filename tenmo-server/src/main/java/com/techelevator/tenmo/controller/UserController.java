package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
public class UserController {

    private static UserDao dao;

    public UserController() {
        this.dao = new JdbcUserDao(DataSource());
    }

    @PreAuthorize("permitAll")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return dao.findAll();
    }

    @RequestMapping (path = "/{user_id}", method = RequestMethod.GET)
    public User getUserByUserId (@PathVariable ("user_id") int userId) {
        return dao.getUserById(userId);
    }

    @RequestMapping (path = "/account/{account_id}", method = RequestMethod.GET)
    public User getUserByAccountId (@PathVariable ("account_id") int accountId) {
        return dao.getUserByAccountId(accountId);
    }

    private BasicDataSource DataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }

}
