package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import sample.data.Account;
import sample.data.Data;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    Data data = Data.getInstance();
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonCreateNewAccount;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {


        textFieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                textFieldName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }

        });

        textFieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldPassword.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (textFieldPassword.getText().length() > 4) {
                textFieldPassword.deleteNextChar();
            }
        });

        textFieldPassword.setOnAction(actionEvent -> {
            login();
        });

        buttonLogin.setOnAction(actionEvent -> {
            login();
        });

        buttonCreateNewAccount.setOnAction(actionEvent -> {
            createNewAccount();
        });

    }

    @FXML
    public void login() {

        data.connectDatabase();

        String name = textFieldName.getText().trim();
        int password = Integer.parseInt(textFieldPassword.getText().trim());

        Account account = data.queryAccountForLogin(name, password);

        if (account != null) {

            try {
                mainBorderPane.getScene().getWindow().hide();
                AccountManagementController controller = new AccountManagementController();
                controller.loadAccountManagement();

            } catch (Exception e) {
                System.out.println("Something gone wrong." +
                        "Account management couldn't load!");
            }
        } else
            loginFailedAlert();

        data.closeDatabase();

    }

    private void loginFailedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText("User name or password is incorrect. Try again!");
        alert.initOwner(mainBorderPane.getScene().getWindow());
        alert.showAndWait();
    }


    @FXML
    public void createNewAccount() {

        data.connectDatabase();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Create New Account");
        dialog.setHeaderText("Create New Account");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newAccountDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        NewAccountController controller = fxmlLoader.getController();

        Account account;
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if ((account = controller.processResult()) != null)
                data.insertAccount(account);
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Create Account failed");
                alert.setHeaderText(null);
                alert.setContentText("You entered something wrong. Try again!");
                alert.initOwner(mainBorderPane.getScene().getWindow());
                alert.showAndWait();
            }
        }

        data.closeDatabase();


    }


}
