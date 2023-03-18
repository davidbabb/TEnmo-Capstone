package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentialsDto;
import com.techelevator.tenmo.services.*;

import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();
    private List <Transfer> pendingTransfers = new ArrayList<>();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: \n");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentialsDto credentials = consoleService.promptForRegistrationCredentials(userService);
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentialsDto credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setAuthToken(currentUser.getToken());
            userService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
        }
        mainMenu();
    }

    private void mainMenu() {
        int menuSelection = -1;
        pendingTransfers = consoleService.getTransferList(currentUser.getUser().getId(), transferService, accountService, userService, true);
        while (menuSelection != 0) {
            consoleService.printMainMenu(pendingTransfers.size());
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
                pendingTransfers = consoleService.getTransferList(currentUser.getUser().getId(), transferService, accountService, userService, true);
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
        currentUser = null;
        accountService.setAuthToken(null);
        userService.setAuthToken(null);
        transferService.setAuthToken(null);
        loginMenu();
    }

	private void viewCurrentBalance() {
		consoleService.printUserBalance(accountService.getBalanceByUserId(currentUser.getUser().getId()));
	}

	private void viewTransferHistory() {
		consoleService.promptForTransferChoice(currentUser, accountService, transferService, userService);
	}

	private void viewPendingRequests() {
		pendingTransfers = consoleService.displayTransfersByUserId(currentUser.getUser().getId(), transferService, accountService, userService, true);
        if (pendingTransfers.isEmpty()) {
            consoleService.listIsEmpty();
        } else {
            String choice = consoleService.approveOrReject();
            if (choice.equals("a")) {
                consoleService.approveTransfer(transferService, pendingTransfers);
            } else if (choice.equals("r")) {
                consoleService.rejectTransfer(transferService, pendingTransfers);
            } else if (!choice.equals("l")) {
                if (consoleService.catchInvalidResponse().equals("a")) {
                    consoleService.approveTransfer(transferService, pendingTransfers);
                } else if (consoleService.approveOrReject().equals("r")) {
                    consoleService.rejectTransfer(transferService, pendingTransfers);
                }
            }
        }
	}

	private void sendBucks() {
        Transfer transfer = new Transfer();
        consoleService.printUsers(currentUser, userService);
		transfer = consoleService.sendBucks(currentUser, transferService, accountService, userService);
        transferService.createTransfer(transfer);
        consoleService.displayTransfer(transfer.getTransferId(), transferService, userService);
	}

	private void requestBucks() {
		Transfer transfer = new Transfer();
        consoleService.printUsers(currentUser, userService);
        transfer = consoleService.requestBucks(currentUser, transferService, accountService, userService, consoleService.promptForUser(currentUser, userService));
        transferService.createTransfer(transfer);
        consoleService.displayTransfer(transfer.getTransferId(), transferService, userService);
	}

}
