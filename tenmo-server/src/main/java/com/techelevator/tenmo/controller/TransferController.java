package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/transfers")
public class TransferController {

    private TransferDao dao;
    private AccountDao accountDao;

    public TransferController() {
        this.dao = new JdbcTransferDao(DataSource());
        this.accountDao = new JdbcAccountDao(DataSource());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Transfer create(@Valid @RequestBody Transfer transfer) {
        return dao.CreateTransfer(transfer);
    }

    @RequestMapping (path = "/account/{account_id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountId (@PathVariable ("account_id") int accountId) {
        return dao.ListTransfersByAccountId(accountId);
    }

    @RequestMapping (path = "/type/{transfer_type_id}", method = RequestMethod.GET)
    public TransferType getTransferTypeById (@PathVariable ("transfer_type_id") int transferTypeId) {
        return dao.getTransferType(transferTypeId);
    }

    @RequestMapping (method = RequestMethod.GET)
    public List <Transfer> getTransfers () {
        return dao.ListTransfers();
    }

    @RequestMapping (method = RequestMethod.PUT)
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer) {
        return dao.updateTransfer(transfer);
    }

    @RequestMapping (path = "/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransferById (@PathVariable("transfer_id") int transferId) {
        return dao.GetTransferById(transferId);
    }

    @RequestMapping (path = "/users/{user_id}", method = RequestMethod.GET)
    public List <Transfer> getTransfersByUserId (@PathVariable("user_id") int userId) {
        return dao.getTransferByUserId(userId);
    }

    @RequestMapping (path = "/{account_id}/{transfer_status_id}", method = RequestMethod.GET)
    public List <Transfer> getTransfersByStatusId (@PathVariable ("account_id") int accountId, @PathVariable ("transfer_status_id") int transferStatusId) {
        return dao.getTransfersByStatusId(transferStatusId, accountId);
    }

    @RequestMapping (path = "/status/{transfer_status_id}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusById (@PathVariable ("transfer_status_id") int transferStatusId) {
        return dao.getTransferStatus(transferStatusId);
    }

        private BasicDataSource DataSource () {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
            dataSource.setUsername("postgres");
            dataSource.setPassword("postgres1");

            return dataSource;
        }


    }

