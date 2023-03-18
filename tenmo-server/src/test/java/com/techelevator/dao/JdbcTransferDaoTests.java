package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests {

    protected static final Transfer TRANSFER_1 = new Transfer(3001, 1, 1, 2001, 2002, BigDecimal.valueOf(20.00));
    protected static final Transfer TRANSFER_2 = new Transfer(3002, 1, 1, 2002, 2003, BigDecimal.valueOf(20.00));
    private static final Transfer TRANSFER_3 = new Transfer(3003, 2, 1, 2001, 2002, BigDecimal.valueOf(20.00));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        sut = new JdbcTransferDao(DataSource());
    }

    @Test
    public void update_transfer_status_to_approved_from_pending() {
        Transfer updatedTransfer = sut.GetTransferById(3001);

        updatedTransfer.setTransferStatusId(2);
        sut.updateTransfer(updatedTransfer);

        Transfer retrievedTransfer = sut.GetTransferById(3001);
        assertTransfersMatch(updatedTransfer, retrievedTransfer);
    }

    @Test
    public void list_transfers_test() {
        List<Transfer> transfers = sut.ListTransfers();
        int size = transfers.size();

        Assert.assertEquals(3, size);
    }

    @Test
    public void list_transfers_by_account_id() {
        List <Transfer> transfers = sut.ListTransfersByAccountId(2001);
        int size = transfers.size();

        Assert.assertEquals(2, size);
    }

    @Test
    public void get_transfer_by_id() {
        Transfer transfer = new Transfer();
        transfer = sut.GetTransferById(3001);

        assertTransfersMatch(transfer, TRANSFER_1);
    }

    @Test
    public void create_transfer() {
        Transfer transfer = new Transfer(3006, 2, 2, 2001, 2002, BigDecimal.valueOf(20.00));
        Transfer newTransfer = sut.CreateTransfer(transfer);

        Assert.assertNotNull(newTransfer);

        Transfer retrievedTransfer = sut.GetTransferById(newTransfer.getTransferId());
    }

    @Test
    public void get_transfer_type_test() {
        TransferType transferType = sut.getTransferType(sut.GetTransferById(3001).getTransferTypeId());
        TransferType type = new TransferType();
        type.setTransferTypeId(1);
        type.setTransferTypeName("Request");

        Assert.assertEquals("", type.getTransferTypeName(), transferType.getTransferTypeName());
    }

    @Test
    public void get_transfer_by_user_test() {
        List <Transfer> transfers = sut.getTransferByUserId(1001);
        Assert.assertEquals(10, transfers.size());
    }

    @Test
    public void update_transfer() {
        Transfer transfer = sut.updateTransfer(TRANSFER_1);
        assertTransfersMatch(transfer, TRANSFER_1);
    }

    @Test
    public void get_transfer_status_id() {
        List <Transfer> transfers = sut.getTransfersByStatusId(2, 2001);
        Assert.assertEquals("", 8, transfers.size());
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }

    private BasicDataSource DataSource () {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdata");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        return dataSource;
    }

}
