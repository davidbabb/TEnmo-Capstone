package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu(int pendingRequests) {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests" + " (" + pendingRequests + ")");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Log Out");
        System.out.println();
    }

    public UserCredentialsDto promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentialsDto(username, password);
    }

    public UserCredentialsDto promptForRegistrationCredentials(UserService userService) {
        String username = promptForString("Username: ");
        if (userService.getAllUsernames().contains(username)) {
            while (userService.getAllUsernames().contains(username)) {
                System.out.println("\nThat username is taken, please choose a different one: ");
                username = promptForString("Username: ");
            }
        }
        String password = promptForString("Password: ");
        return new UserCredentialsDto(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void printUserBalance (BigDecimal balance) {
        System.out.println("\nYour current balance is: TE" + balance);
    }

    public void printUsers (AuthenticatedUser currentUser, UserService userService) {
        User[] users = userService.getAllUsers();
        System.out.println("--------------------------------\n" + "Users" +
                "");
        System.out.println("--------------------------------");
        System.out.printf("%-10s %5s", "ID", "Name\n\n");


        for (User user : users) {
            if (currentUser.getUser().getUsername() != user.getUsername()) {
                System.out.printf("%-10s %-10s", user.getId(), user.getUsername());
                System.out.println();
            }
        }
        System.out.println("-------");
    }

    public int promptForUser (AuthenticatedUser currentUser, UserService userService) {
        System.out.print("Please enter the user ID of the sender: ");
        int choice = 0;
        boolean running = true;
        while (running) {

            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == currentUser.getUser().getId()) {
                    System.out.println("You cannot request money from yourself.\n" +
                            "\nPlease enter another ID: ");
                    choice = Integer.parseInt(scanner.nextLine());
                } else if (!userService.getUserIdList().contains(choice)) {

                    while (!userService.getUserIdList().contains(choice)) {
                        try {
                            System.out.println("Please enter a valid User ID: ");
                            choice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid User ID: ");
                            choice = Integer.parseInt(scanner.nextLine());
                        }
                    }

                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid User ID: ");
                choice = Integer.parseInt(scanner.nextLine());
            }

            running = false;
        }

        return choice;
    }

    public Transfer sendBucks (AuthenticatedUser currentUser, TransferService transferService, AccountService accountService, UserService userService) {
        System.out.print("Please enter the user ID of the receiver: ");
        int choice = 0;
        boolean running = true;
        while (running) {

            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == currentUser.getUser().getId()) {
                    System.out.println("You cannot send money to yourself.\n" +
                            "\nPlease enter another ID: ");
                    choice = Integer.parseInt(scanner.nextLine());
                } else if (!userService.getUserIdList().contains(choice)) {

                    while (!userService.getUserIdList().contains(choice)) {
                        try {
                            System.out.println("Please enter a valid User ID: ");
                            choice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid User ID: ");
                            choice = Integer.parseInt(scanner.nextLine());
                        }
                    }

                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid User ID: ");
                choice = Integer.parseInt(scanner.nextLine());
            }

            running = false;
        }

        User user = userService.getUserByUserId(choice);
        System.out.println("\nPlease enter the amount of TE you want to send to " + user.getUsername() + ": ");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
        BigDecimal currentBalance = accountService.getBalanceByUserId(currentUser.getUser().getId());
        Account fromAccount = accountService.getAccountsByUserId(currentUser.getUser().getId());
        Account toAccount = accountService.getAccountsByUserId(user.getId());

        if (amount.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            while (currentBalance.compareTo(amount) < 0) {
                System.out.println("You cannot send more TE than you have.\n\n" + "Please enter an amount less than TE" + currentBalance + ": ");
                amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
            }
        } else {
            System.out.println("Please enter an amount greater than TE0.00: ");
            amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
        }
        int transferId = transferService.setMaxTransferId();
        Transfer transfer = new Transfer(transferId, 2, 2, fromAccount.getAccountId(), toAccount.getAccountId(), amount);
        return transfer;
    }

    public Transfer requestBucks (AuthenticatedUser currentUser, TransferService transferService, AccountService accountService, UserService userService, int senderId) {
        User user = userService.getUserByUserId(senderId);
        System.out.println("\nPlease enter the amount of TE you want to request from " + user.getUsername() + ": ");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
        BigDecimal currentBalance = accountService.getBalanceByUserId(user.getId());
        Account fromAccount = accountService.getAccountsByUserId(user.getId());
        Account toAccount = accountService.getAccountsByUserId(currentUser.getUser().getId());

        if (amount.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            while (currentBalance.compareTo(amount) < 0) {
                System.out.println("You cannot request more TE than " + user.getUsername() + " has.\n\n" + "Please enter an amount less than TE" + currentBalance + ": ");
                amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
            }
        } else {
            System.out.println("Please enter an amount greater than TE0.00: ");
            amount = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine()));
        }
        int transferId = transferService.setMaxTransferId();
        Transfer transfer = new Transfer(transferId, 1, 1, fromAccount.getAccountId(), toAccount.getAccountId(), amount);
        return transfer;
    }

    public void displayTransfer (int transferId, TransferService transferService, UserService userService) {
        Transfer transfer = transferService.getTransferByTransferId(transferId);
        System.out.println("--------------------------------\n" + "Transfer Details\n" + "--------------------------------\n");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + userService.getUserByAccountId(transfer.getAccountFrom()).getUsername());
        System.out.println("To: " + userService.getUserByAccountId(transfer.getAccountTo()).getUsername());
        System.out.println("Type: " + transferService.getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeName());
        System.out.println("Status: " + transferService.getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc());
        System.out.println("Amount: TE" + transfer.getAmount());
    }

    public List <Transfer> displayTransfersByUserId (int userId, TransferService transferService, AccountService accountService, UserService userService, boolean pending) {
        List <Transfer> transferList = new ArrayList<>();
        Transfer[] transfers = transferService.getTransfersById(userId);
        System.out.println("--------------------------------\n" + "Transfer Details\n" + "--------------------------------\n");

        if (pending) {
            for (Transfer transfer: transfers) {
                if (transferService.getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc().equals("Pending")) {
                    if ((transferService.getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeName().equals("Request") && transfer.getAccountFrom() == accountService.getAccountsByUserId(userId).getAccountId())) {
                        System.out.println("Id: " + transfer.getTransferId());
                        System.out.println("From: " + userService.getUserByAccountId(transfer.getAccountFrom()).getUsername());
                        System.out.println("To: " + userService.getUserByAccountId(transfer.getAccountTo()).getUsername());
                        System.out.println("Type: " + transferService.getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeName());
                        System.out.println("Status: " + transferService.getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc());
                        System.out.println("Amount: TE" + transfer.getAmount());
                        System.out.println("--------------------------------");
                        transferList.add(transfer);
                    }
                }
            }
        } else {
            for (Transfer transfer : transfers) {
                System.out.println("Id: " + transfer.getTransferId());
                System.out.println("From: " + userService.getUserByAccountId(transfer.getAccountFrom()).getUsername());
                System.out.println("To: " + userService.getUserByAccountId(transfer.getAccountTo()).getUsername());
                System.out.println("Type: " + transferService.getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeName());
                System.out.println("Status: " + transferService.getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc());
                System.out.println("Amount: TE" + transfer.getAmount());
                System.out.println("--------------------------------");
                transferList.add(transfer);
            }
        }

        return transferList;
    }

    public List <Transfer> getTransferList (int userId, TransferService transferService, AccountService accountService, UserService userService, boolean pending) {
        List <Transfer> transferList = new ArrayList<>();
        Transfer[] transfers = transferService.getTransfersById(userId);

        if (pending) {
            for (Transfer transfer: transfers) {
                if (transferService.getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc().equals("Pending")) {
                    if ((transferService.getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeName().equals("Request") && transfer.getAccountFrom() == accountService.getAccountsByUserId(userId).getAccountId())) {
                        transferList.add(transfer);
                    }
                }
            }
        } else {
            for (Transfer transfer : transfers) {
                transferList.add(transfer);
            }
        }

        return transferList;
    }

    public void approveTransfer (TransferService transferService, List <Transfer> transferList) {
        int nextChoice = 0;
        boolean running = true;
        Transfer updatedTransfer = new Transfer();
        boolean success = false;

        if (!transferList.isEmpty()) {

            while (running) {
                System.out.println("Please enter the number of the transfer you want to approve: ");
                nextChoice = Integer.parseInt(scanner.nextLine());
                for (Transfer transfer : transferList) {
                    if (transfer.getTransferId() == nextChoice) {
                        updatedTransfer = transferService.getTransferByTransferId(nextChoice);
                        updatedTransfer.setTransferStatusId(2);
                        transferService.update(updatedTransfer);
                        System.out.println("Transfer " + nextChoice + " approved.");
                        success = true;
                        running = false;
                    }

                }
                if (!success) {
                    while (!transferList.contains(transferService.getTransferByTransferId(nextChoice))) {
                        System.out.println("Please enter a valid Transfer ID: ");
                        nextChoice = Integer.parseInt(scanner.nextLine());
                    }
                    System.out.println("Are you sure you would like to approve Transfer " + nextChoice + "? (Y) or (N): ");
                    String choice = scanner.nextLine().toLowerCase();
                    if (choice.equals("y")) {
                        updatedTransfer = transferService.getTransferByTransferId(nextChoice);
                        updatedTransfer.setTransferStatusId(2);
                        transferService.update(updatedTransfer);
                    } else if (choice.equals("n")) {
                        running = false;
                    }

                }
            }

        }

    }

    public String approveOrReject () {
        System.out.println("Would you like to (A)pprove, (R)eject, or (L)eave your transfers as is? Type the corresponding letter for your choice: ");
        String choice = scanner.nextLine().toLowerCase();
        return choice;
    }

    public String catchInvalidResponse () {
        String choice = null;
        while (!choice.equals("a") || !choice.equals("r") || !choice.equals("l")) {
            System.out.println("Please enter (A)pprove, (R)eject, or (L)eave: ");
            scanner.nextLine().toLowerCase();
        }

        return choice;
    }

    public void listIsEmpty() {
        System.out.println("You have no pending transfers.");
    }

    public void rejectTransfer (TransferService transferService, List <Transfer> transferList) {
        int nextChoice = 0;
        boolean running = true;
        Transfer updatedTransfer = new Transfer();
        boolean success = true;

        if (!transferList.isEmpty()) {

            while (running) {
                System.out.println("Please enter the number of the transfer you want to reject: ");
                nextChoice = Integer.parseInt(scanner.nextLine());
                for (Transfer transfer : transferList) {
                    if (transfer.getTransferId() == nextChoice) {
                        updatedTransfer = transferService.getTransferByTransferId(nextChoice);
                        updatedTransfer.setTransferStatusId(3);
                        transferService.update(updatedTransfer);
                        System.out.println("Transfer " + nextChoice + " rejected.");
                        success = true;
                        running = false;
                    }

                }
                if (!success) {
                    while (!transferList.contains(transferService.getTransferByTransferId(nextChoice))) {
                        System.out.println("Please enter a valid Transfer ID: ");
                        nextChoice = Integer.parseInt(scanner.nextLine());
                    }
                    System.out.println("Are you sure you would like to reject Transfer " + nextChoice + "? (Y) or (N): ");
                    String choice = scanner.nextLine().toLowerCase();
                    if (choice.equals("y")) {
                        updatedTransfer = transferService.getTransferByTransferId(nextChoice);
                        updatedTransfer.setTransferStatusId(3);
                        transferService.update(updatedTransfer);
                    } else if (choice.equals("n")) {
                        running = false;
                    }

                }
            }

        }

    }

    public void promptForTransferChoice(AuthenticatedUser currentUser, AccountService accountService, TransferService transferService, UserService userService) {
        System.out.println("\n1: View transfer history\n" + "2: Search by Transfer ID\n\n" + "Please enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        int nextChoice = 0;
        if (choice == 1) {
            displayTransfersByUserId(currentUser.getUser().getId(), transferService, accountService, userService, false);
        } else if (choice == 2) {
            System.out.println("Please enter a valid transfer ID: ");
            nextChoice = Integer.parseInt(scanner.nextLine());
            displayTransfer(nextChoice, transferService, userService);
        } else {
            while (choice > 2 || choice < 1) {
                System.out.println("Please enter a valid choice: ");
                choice = Integer.parseInt((scanner.nextLine()));
            }
            if (choice == 1) {
                displayTransfersByUserId(currentUser.getUser().getId(), transferService, accountService, userService, false);
            } else {
                System.out.println("Please enter a valid transfer ID: ");
                nextChoice = Integer.parseInt(scanner.nextLine());
                displayTransfer(nextChoice, transferService, userService);
            }

        }
    }
    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
