package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.data.Account;
import sample.data.Data;

public class AccountManagementController {
    Data data = Data.getInstance();
    Account loggedAccount = Data.getInstance().getAccountList().get(0);

    @FXML
    private AnchorPane accountManagementStage;
    @FXML
    private Label labelAccountName;
    @FXML
    private Label labelAccountNumber;
    @FXML
    private Label labelAccountPin;
    @FXML
    private Label labelAccountAmount;
    @FXML
    private Button buttonWithdraw;
    @FXML
    private Button buttonDeposit;
    @FXML
    private Button buttonTransfer;


    public void initialize() {

        buttonDeposit.setOnAction(actionEvent -> {

            data.connectDatabase();

            loggedAccount.deposit(100);
            boolean result = data.updateAmountForAccount(loggedAccount.getAccountName(), loggedAccount.getAccountPin(), loggedAccount.getAmount());
            if (result)
                displayAccountData(loggedAccount);
            else
                System.out.println("Can't update amount or can't deposit money");

            data.closeDatabase();
        });

        buttonWithdraw.setOnAction(actionEvent -> {
            data.connectDatabase();
            if (loggedAccount.getAmount() > 0) {
                loggedAccount.withdraw(100);
                boolean result = data.updateAmountForAccount(loggedAccount.getAccountName(), loggedAccount.getAccountPin(), loggedAccount.getAmount());
                if (result)
                    displayAccountData(loggedAccount);
                else
                    System.out.println("Can't update amount or can't withdraw money");
            }

            data.closeDatabase();
        });

    }

    public void loadAccountManagement() {

        AccountManagementController controller = load();
        controller.displayAccountData(loggedAccount);

    }


    private void displayAccountData(Account account) {
        labelAccountName.setText(account.getAccountName());
        labelAccountNumber.setText(Integer.toString(account.getAccountNumber()));
        labelAccountPin.setText(Integer.toString(account.getAccountPin()));
        labelAccountAmount.setText(Double.toString(account.getAmount()));
    }


    public AccountManagementController load() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("accountManagement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Account Management");
            stage.setScene(scene);
            stage.show();

            AccountManagementController controller = fxmlLoader.getController();
            return controller;


        } catch (Exception e) {
            System.out.println("Couldn't load account info");
            return null;
        }

    }


}
