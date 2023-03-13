package com.techelevator.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    Transfer transfer = new Transfer();

    @Test
    public void transferId_returns_5() {
        transfer.setTransferId(5);
        assertEquals(transfer.getTransferId(), 5);
    }

    @Test
    public void transferTypeId_returns_5() {
        transfer.setTransferTypeId(1);
        assertEquals(transfer.getTransferTypeId(), 1);
    }

    @Test
    public void TransferStatusId_returns_5() {
        transfer.setTransferStatusId(1);
        assertEquals(transfer.getTransferStatusId(), 1);
    }

    @Test
    public void amount_returns_5() {
        transfer.setAmount(BigDecimal.valueOf(5));
        assertEquals(transfer.getAmount(), BigDecimal.valueOf(5));
    }

    @Test
    public void accountFrom_returns_correct() {
        transfer.setAccountFrom(1000);
        assertEquals(transfer.getAccountFrom(), 1000);
    }

    @Test
    public void accountTo_returns_correct() {
        transfer.setAccountTo(1000);
        assertEquals(transfer.getAccountTo(), 1000);
    }

}
