package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests {

    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(820.00));
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(1180.00));
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(1000.00));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        sut = new JdbcAccountDao(DataSource());
    }

    @Test
    public void list_accounts_test() {
        List<Account> accounts = sut.ListAccounts();
        Assert.assertEquals(3, accounts.size());
    }

    @Test
    public void get_account_by_user() {
        Account account = sut.GetAccountByUser(1001);
        assertAccountsMatch(ACCOUNT_1, account);
    }

    @Test
    public void get_account_by_account() {
        Account account = sut.GetAccountByAccountId(2002);
        assertAccountsMatch(ACCOUNT_2, account);
    }



    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

    private BasicDataSource DataSource () {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdata");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }

}
