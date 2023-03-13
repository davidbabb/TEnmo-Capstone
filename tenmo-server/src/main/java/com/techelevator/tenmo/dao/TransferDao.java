package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;

import java.util.List;

public interface TransferDao {

    List <Transfer> ListTransfers();

    Transfer CreateTransfer(Transfer transfer);

    TransferType getTransferType(int transferTypeId);

    List <Transfer> ListTransfersByAccountId(int accountId);

    Transfer GetTransferById(int transferId) ;

    public List <Transfer> getTransferByUserId (int userId);

    //public Transfer updateTransfer (Transfer updatedTransfer);

    List <Transfer> getTransfersByStatusId (int transferStatusId, int accountId);

    Transfer updateTransfer (Transfer transfer);

    TransferStatus getTransferStatus (int transferStatusId);
}
