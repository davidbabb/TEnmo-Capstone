package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.security.Principal;

public class Transfer {

    @JsonProperty("transfer_id")
    private int transferId;
    @JsonProperty("transfer_type_id")
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "2.00")
    private int transferTypeId;
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "3.00")
    @JsonProperty("transfer_status_id")
    private int transferStatusId;
    @JsonProperty("account_from")
    private int accountFrom;
    @JsonProperty("account_to")
    private int accountTo;
    @DecimalMin(value = "1.00", message = "The field should be TE1.00 or greater.")
    private BigDecimal amount;

    public Transfer (int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer(){}

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
