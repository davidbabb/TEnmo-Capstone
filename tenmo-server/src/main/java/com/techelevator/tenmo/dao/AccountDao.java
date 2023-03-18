package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> ListAccounts();

    Account GetAccountByUser (int id);

    Account GetAccountByAccountId (int id);

    void AddToBalance (int accountId, BigDecimal amount);

    void SubtractFromBalance(int accountId, BigDecimal amount);

}
