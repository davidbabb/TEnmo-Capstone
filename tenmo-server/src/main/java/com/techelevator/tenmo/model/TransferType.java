package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferType {

    @JsonProperty ("transfer_type_id")
    private int transferTypeId;
    @JsonProperty ("transfer_type_desc")
    private String transferTypeName;



    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeName() {
        return transferTypeName;
    }

    public void setTransferTypeName(String transferTypeName) {
        this.transferTypeName = transferTypeName;
    }
}
