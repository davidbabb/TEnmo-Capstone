package com.techelevator.dao;

import com.techelevator.tenmo.model.Authority;
import com.techelevator.tenmo.model.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    User user = new User();

    @Test
    public void userId_equal_to_5() {
        int id = 5;
        user.setId(id);
        assertEquals(user.getId(), id);
    }

    @Test
    public void username_equals_Hello() {
        user.setUsername("Hello");
        assertEquals(user.getUsername(), "Hello");
    }

    @Test
    public void password_equals_Hello() {
        user.setPassword("Hello");
        assertEquals(user.getPassword(), "Hello");
    }

    @Test
    public void isActivated_equals_true() {
        user.setActivated(true);
        assertEquals(user.isActivated(), true);
    }

    @Test
    public void setAuthorities_equals_hello_goodbye() {
        Set<Authority> authority = new HashSet<Authority>();
        Set<Authority> authority2 = new HashSet<Authority>();
        user.setAuthorities(authority);
        assertEquals(user.getAuthorities(), authority2);

    }
}
