package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.managers.console.ConsolePrinter;
import main.network.TcpClient;
import main.scenes.LoginListener;
import main.scenes.LoginStatusListener;

public class LoginController {

    private String login;
    private String password;
    //BUTTONS
    private final String LOGIN_BUTTON_TEXT = "Log in!";
    private final String REGISTER_BUTTON_TEXT = "Register!";
    private final String LOGGING_IN_BUTTON_TEXT = "Logging in..";
    //COMMANDS
    private final String LOGIN_COMMAND = "/login ";
    private final String REGISTER_COMMAND = "/register ";
    //STH WENT WRONG TEXT
    private final String WRONG_PASSWORD = "Wrong username or password.";
    private final String LOGIN_TOO_LONG = "Login can't exceed 20 characters";
    private final String LOST_CONNECTION = "Connection has been lost. Trying to reconnect..";
    //BOOLEANS
    private boolean isLoggingInInProgress = false;

    private String actionType = LOGIN_BUTTON_TEXT;

    private TcpClient client;
    private LoginListener loginListener;

    public void construct(TcpClient client, LoginListener loginListener) {
        this.client = client;
        this.loginListener = loginListener;
        client.setLoginStatusListener(new LoginStatusListener() {
            @Override
            public void onLoginStatusReceived() {
                if (client.isClientLoggedIn()) {
                    onSuccessfullyLogged();
                } else if (!client.isClientLoggedIn()) {
                    onWrongPassword();
                }
            }
        });
    }

    @FXML
    protected Button switchRegisterButton;
    @FXML
    protected Label changeActionTypeLabel;
    @FXML
    protected Label somethingWentWrong;
    @FXML
    protected Button loginButton;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField loginField;

    @FXML
    protected void onLoginButtonClick() {
        if(client.isClientConnected()){
            if (!isLoggingInInProgress) {
                ConsolePrinter.loginAttempt();
                login = loginField.getText();
                password = passwordField.getText();
                isLoggingInInProgress = true;

                if(isLoginTooLong()){
                    Platform.runLater(() -> {
                        somethingWentWrong.setText(LOGIN_TOO_LONG);
                    });
                } else if (loginAndPasswordCanBeSend()) {
                    if (client.isClientConnected()) {
                        loginButton.setText(LOGGING_IN_BUTTON_TEXT);
                        if (actionType.equals(LOGIN_BUTTON_TEXT)) {
                            client.sendMessage(getLoginCommand());
                        } else if (actionType.equals(REGISTER_BUTTON_TEXT)) {
                            client.sendMessage(getRegisterCommand());
                        }
                    }
                }
                isLoggingInInProgress = false;
            }
        }else {
            Platform.runLater(() -> {
                somethingWentWrong.setText(LOST_CONNECTION);
            });
        }
    }

    @FXML
    protected void onSwitchButtonClick() {
        if (actionType.equals(LOGIN_BUTTON_TEXT)) {
            actionType = REGISTER_BUTTON_TEXT;
            loginButton.setText(REGISTER_BUTTON_TEXT);
            switchRegisterButton.setText("Switch to login");
            changeActionTypeLabel.setText("If you have an existing account click here:");
        } else {
            actionType = LOGIN_BUTTON_TEXT;
            loginButton.setText(LOGIN_BUTTON_TEXT);
            switchRegisterButton.setText("Switch to register");
            changeActionTypeLabel.setText("If you don't have account yet, register click here:");
        }

    }

    public String getUserName() {
        return login;
    }

    private String getLoginCommand() {
        return LOGIN_COMMAND + login + " " + password;
    }

    private String getRegisterCommand() {
        return REGISTER_COMMAND + login + " " + password;
    }

    private void onSuccessfullyLogged() {
        ConsolePrinter.loggedIn();
        loginListener.onClientLoggedIn();
        Platform.runLater(() -> {
            passwordField.setText("");
            loginButton.setText(actionType);
        });
    }

    private void onWrongPassword() {
        Platform.runLater(() -> {
            if (!somethingWentWrong.getText().equals(WRONG_PASSWORD)) {
                somethingWentWrong.setText(WRONG_PASSWORD);
            }
            passwordField.setText("");
            loginButton.setText(actionType);
        });
    }

    private boolean isLoginTooLong(){
        return login != null && login.length() > 20;
    }

    private boolean loginAndPasswordCanBeSend(){
        return login != null && !login.isBlank() && password != null && !password.isBlank();
    }
}
