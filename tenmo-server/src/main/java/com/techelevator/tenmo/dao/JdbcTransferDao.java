package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private JdbcAccountDao dao;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dao = new JdbcAccountDao(dataSource);
    }

    @Override
    public List<Transfer> ListTransfers () {
        List <Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List <Transfer> ListTransfersByAccountId (int accountId) {
        List <Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer GetTransferById (int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        if (result.next()) {
            transfer = mapRowToTransfer(result);
        } else {
            return null;
        }

        return transfer;

    }

    @Override
    public Transfer CreateTransfer(Transfer transfer) {

        if (transfer.getTransferStatusId() == 2 && transfer.getTransferTypeId() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Requests cannot be created as 'Approved'.");
        } else if (transfer.getTransferTypeId() == 2 && transfer.getTransferStatusId() != 2){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Send transfers are automatically approved.");
        }

        else {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                    "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
            Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

            if (transferId == null) {
                return null;
            } else {
                transfer.setTransferId(transferId);
                if (transfer.getTransferStatusId() == 2) {
                    Account accountTo = dao.GetAccountByAccountId(transfer.getAccountTo());
                    Account accountFrom = dao.GetAccountByAccountId(transfer.getAccountFrom());
                    try {
                        if (accountFrom.getBalance().compareTo(transfer.getAmount()) > 0) {
                            dao.AddToBalance(accountTo.getAccountId(), transfer.getAmount());
                            dao.SubtractFromBalance(accountFrom.getAccountId(), transfer.getAmount());
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        if (accountFrom == accountTo) {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot request money from yourself.");
                        } else {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The sender has insufficient funds.");

                        }
                    }
                }
                return transfer;
            }

        }

    }

    @Override
    public TransferType getTransferType (int transferTypeId) {
        TransferType transferType = new TransferType();
        String sql = "SELECT * FROM transfer_type WHERE transfer_type_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferTypeId);

        if (result.next()) {
            transferType = mapRowToTransferType(result);
        }

        return transferType;
    }

    @Override
    public List <Transfer> getTransferByUserId (int userId) {
        List <Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer t \n" +
                "JOIN account a ON a.account_id = t.account_from\n" +
                "JOIN tenmo_user tu ON tu.user_id = a.user_id\n" +
                "WHERE a.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        String sql2 = "SELECT * FROM transfer t \n" +
                "JOIN account a ON a.account_id = t.account_to\n" +
                "JOIN tenmo_user tu ON tu.user_id = a.user_id\n" +
                "WHERE a.user_id = ?;";
        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, userId);

        while (results2.next()) {
            Transfer transfer = mapRowToTransfer(results2);
            transfers.add(transfer);
        }

        return transfers;

    }

    @Override
    public Transfer updateTransfer (Transfer transfer) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " + "WHERE transfer_id = ?; ";
        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
        Account accountTo = dao.GetAccountByAccountId(transfer.getAccountTo());
        Account accountFrom = dao.GetAccountByAccountId(transfer.getAccountFrom());
        if (transfer.getTransferStatusId() == 2) {
            try {
                if (accountFrom.getBalance().compareTo(transfer.getAmount()) > 0) {
                    dao.AddToBalance(accountTo.getAccountId(), transfer.getAmount());
                    dao.SubtractFromBalance(accountFrom.getAccountId(), transfer.getAmount());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                if (accountFrom == accountTo) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot request money from yourself.");
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The sender has insufficient funds.");

                }
            }
        }
        return transfer;
    }

    @Override
    public List <Transfer> getTransfersByStatusId (int transferStatusId, int accountId) {
        List <Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, t.transfer_status_id, account_from, account_to, amount " +
                "FROM transfer t JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE t.transfer_status_id = ? AND account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatusId, accountId);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        String sql2 = "SELECT transfer_id, transfer_type_id, t.transfer_status_id, account_from, account_to, amount " +
                "FROM transfer t JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE t.transfer_status_id = ? AND account_to = ?;";
        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, transferStatusId, accountId);

        while (results2.next()) {
            Transfer transfer = mapRowToTransfer(results2);
            transfers.add(transfer);
        }

        return transfers;

    }

    @Override
    public TransferStatus getTransferStatus (int transferStatusId) {
        TransferStatus transferStatus = new TransferStatus();
        String sql = "SELECT * FROM transfer_status WHERE transfer_status_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);

        if (result.next()) {
            transferStatus = mapRowToTransferStatus(result);
        }

        return transferStatus;
    }

    private int GetMaxId() {
        int maxId = 0;
        List <Transfer> transfers = ListTransfers();
        for (Transfer transfer: transfers) {
            if (transfer.getTransferId() > maxId) {
                maxId = transfer.getTransferId();
            }
        }

        return maxId;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

    private TransferType mapRowToTransferType(SqlRowSet rs) {
        TransferType transferType = new TransferType();
        transferType.setTransferTypeId(rs.getInt("transfer_type_id"));
        transferType.setTransferTypeName(rs.getString("transfer_type_desc"));
        return transferType;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet rs) {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusId(rs.getInt("transfer_status_id"));
        transferStatus.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        return transferStatus;
    }
}
