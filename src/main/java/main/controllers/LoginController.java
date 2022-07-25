package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.network.Client;

public class LoginController implements LoggingInController {

    private String login;
    private String password;
    protected Client client;
    final String LOGIN = "login";
    final String REGISTER = "register";
    String loginType = LOGIN;

    @FXML
    protected Button switchRegisterButton;
    @FXML
    public Label connectionProblems;
    @FXML
    protected Label changeLoginTypeLabel; //Label name to change
    @FXML
    protected Label wrongPassword;
    @FXML
    protected Button loginButton;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField loginField;

    @FXML
    protected void onButtonClick() {
        login = loginField.getText();
        password = passwordField.getText();

        if (loginType.equals(LOGIN)) {
            client.sendMessage(getLoginCommand());
        } else if (loginType.equals(REGISTER)) {
            client.sendMessage(getRegisterCommand());
        }
    }

    @FXML
    protected void onSwitchButtonClick() {
        if (loginType.equals(LOGIN)) {
            loginType = REGISTER;
            loginButton.setText("Register!");
            switchRegisterButton.setText("Switch to login");
            changeLoginTypeLabel.setText("If you have an existing account click here:");
        } else {
            loginType = LOGIN;
            loginButton.setText("Log in!");
            switchRegisterButton.setText("Switch to register");
            changeLoginTypeLabel.setText("If you don't have account yet, register click here:");
        }
    }

    @Override
    public String getLoginCommand() {
        return "/login " + login + " " + password;
    }

    public String getRegisterCommand() {
        return "/register " + login + " " + password;
    }


    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLoginToNull() {
        login = null;
    }

    @Override
    public Label getSomethingWentWrongLabel() {
        return wrongPassword;
    }

    public void construct(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
