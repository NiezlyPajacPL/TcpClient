package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.managers.Delay;
import main.network.Client;
import main.scenes.login.LoginListener;
import main.scenes.login.LoginThread;


public class LoginController {

    private String login;
    private String password;
    final String LOGIN = "Log in!";
    final String REGISTER = "Register!";
    String loginType = LOGIN;

    public Client client;
    public LoginListener loginListener;
    private LoginThread loginThread;
    private Thread thread;
    protected String userName;

    @FXML
    protected Button switchRegisterButton;
    @FXML
    public Label connectionProblems;
    @FXML
    protected Label changeLoginTypeLabel; //Label name to change
    @FXML
    protected Label somethingWentWrong;
    @FXML
    protected Button loginButton;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField loginField;

    public void construct(Client client, LoginListener loginListener) {
        this.client = client;
        this.loginListener = loginListener;
    }
    @FXML
    protected void onButtonClick() {
    if(!loginButton.getText().equals("Logging in...")) {

        login = loginField.getText();
        password = passwordField.getText();
        if (client.isClientConnected()) {
            loginButton.setText("Logging in..");
            if (loginType.equals(LOGIN)) {
                client.sendMessage(getLoginCommand());
            } else if (loginType.equals(REGISTER)) {
                client.sendMessage(getRegisterCommand());
            }
        }

        Delay.delay(2000, new Runnable() {
            @Override
            public void run() {
                if (!client.isClientConnected()) {
                    onConnectionLost();
                } else if (!client.isClientLoggedIn()) {
                    onWrongPassword();
                } else if (client.isClientLoggedIn()) {
                    onSuccessfullyLogged();
                }
                loginButton.setText(loginType);
            }
        });
    }
    }

    @FXML
    protected void onSwitchButtonClick() {
        if (loginType.equals(LOGIN)) {
            loginType = REGISTER;
            loginButton.setText(REGISTER);
            switchRegisterButton.setText("Switch to login");
            changeLoginTypeLabel.setText("If you have an existing account click here:");
        } else {
            loginType = LOGIN;
            loginButton.setText(LOGIN);
            switchRegisterButton.setText("Switch to register");
            changeLoginTypeLabel.setText("If you don't have account yet, register click here:");
        }

    }


    public String getUserName() {
        return login;
    }

    private String getLoginCommand() {
        return "/login " + login + " " + password;
    }

    private String getRegisterCommand() {
        return "/register " + login + " " + password;
    }

    private void onSuccessfullyLogged() {
        System.out.println("Log: Client logged in, closing login thread.");
        loginListener.onClientLoggedIn();
    }

    private void onWrongPassword() {
        final String WRONG_PASSWORD = "Wrong username or password.";
        if (!somethingWentWrong.getText().equals(WRONG_PASSWORD)) {
            somethingWentWrong.setText(WRONG_PASSWORD);
            passwordField.setText("");
        }
    }

    private void onConnectionLost(){
        final String LOST_CONNECTION = "Connection has been lost. Trying to reconnect..";
        if (!somethingWentWrong.getText().equals(LOST_CONNECTION)) {
            somethingWentWrong.setText(LOST_CONNECTION);
        }
    }
}
