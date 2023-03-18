package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferStatus {

    @JsonProperty ("transfer_status_id")
    private int transferStatusId;
    @JsonProperty ("transfer_status_desc")
    private String transferStatusDesc;

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

}
