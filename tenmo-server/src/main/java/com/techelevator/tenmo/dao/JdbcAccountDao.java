package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    private static List<Account> accounts = new ArrayList<>();

    public JdbcAccountDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Account> ListAccounts(){
        List <Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }

        return accounts;
    }

    @Override
    public Account GetAccountByUser (int id){
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        if (results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }

    @Override
    public Account GetAccountByAccountId (int id) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public void AddToBalance (int accountId, BigDecimal amount) {
        Account account = GetAccountByAccountId(accountId);
        account.setBalance(account.getBalance().add(amount));
        BigDecimal balanceAfter = account.getBalance();
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, balanceAfter, accountId);
    }

    @Override
    public void SubtractFromBalance (int accountId, BigDecimal amount) {
        Account account = GetAccountByAccountId(accountId);
        account.setBalance(account.getBalance().subtract(amount));
        BigDecimal balanceAfter = account.getBalance();
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, balanceAfter, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
