package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import sample.data.Account;

public class NewAccountController {

    @FXML
    private TextField accountNameTextField;

    @FXML
    private TextField accountNumberTextField;

    @FXML
    private TextField accountPinTextField;
    @FXML
    private DialogPane dialogPane;


    public void initialize() {
        accountNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                accountNameTextField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });

        accountNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                accountNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (accountNumberTextField.getText().length()>6){
                accountNumberTextField.deleteNextChar();
            }
        });

        accountPinTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                accountPinTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (accountPinTextField.getText().length()>4){
                accountPinTextField.deleteNextChar();
            }
        });

    }

    public boolean checkResult() {
        if (accountNameTextField.getText().isEmpty()
                || (accountNumberTextField.getText().isEmpty()
                || accountNumberTextField.getText().trim().length() != 6)
                || (accountPinTextField.getText().isEmpty()
                || accountPinTextField.getText().trim().length() != 4)) {
            return false;
        } else {
            return true;
        }


    }

    public Account processResult() {

        if (checkResult()) {
            String name = accountNameTextField.getText().trim();
            int number = Integer.parseInt(accountNumberTextField.getText().trim());
            int pin = Integer.parseInt((accountPinTextField.getText().trim()));
            Account account = new Account(name, number, pin);
            return account;
        } else
            return null;
    }


}
