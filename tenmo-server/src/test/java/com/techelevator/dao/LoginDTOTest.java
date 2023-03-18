package com.techelevator.dao;

import com.techelevator.tenmo.model.LoginDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginDTOTest {

    LoginDto login = new LoginDto();

    @Test
    public void get_set_user_equals_Hello() {
        login.setUsername("david");
        assertEquals(login.getUsername(), "david");
    }

    @Test
    public void get_set_Password_equals_babb() {
        login.setPassword("babb");
        assertEquals(login.getPassword(), "babb");
    }

}
