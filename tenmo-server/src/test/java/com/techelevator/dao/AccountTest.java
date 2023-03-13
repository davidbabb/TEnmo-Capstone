package com.techelevator.dao;

import com.techelevator.tenmo.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

    Account account = new Account();

    @Test
    public void set_get_accountid() {
        account.setAccountId(10);
        assertEquals(account.getAccountId(), 10);
    }

    @Test
    public void set_get_userId() {
        account.setUserId(10);
        assertEquals(account.getUserId(), 10);
    }

    @Test
    public void set_get_accountBalance() {
        account.setBalance(BigDecimal.valueOf(10));
        assertEquals(account.getBalance(), BigDecimal.valueOf(10));
    }
}
