package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.network.Client;
import main.scenes.login.LoginListener;
import main.scenes.login.LoginThread;
import main.scenes.login.WrongPasswordListener;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class LoginController{

    private String login;
    private String password;
    final String LOGIN = "login";
    final String REGISTER = "register";
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
    protected Label wrongPassword;
    @FXML
    protected Button loginButton;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField loginField;

    WrongPasswordListener wrongPasswordListener = () -> {
        if (!wrongPassword.getText().equals("Wrong username or password.")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    wrongPassword.setText("Wrong username or password.");
               //     loginField.setText("");
                    passwordField.setText("");
                }
            });
        }
    };

    @FXML
    protected void onButtonClick() {
        if (!thread.isAlive()) {
            loginThread = new LoginThread(client, loginListener, wrongPasswordListener);
            thread = new Thread(loginThread);
            thread.start();
        }

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

    private String getLoginCommand() {
        return "/login " + login + " " + password;
    }

    private String getRegisterCommand() {
        return "/register " + login + " " + password;
    }

    public void construct(Client client, LoginListener loginListener) {
        this.client = client;
        this.loginListener = loginListener;

        loginThread = new LoginThread(client, loginListener, wrongPasswordListener);
        thread = new Thread(loginThread);
    }

    public String getUserName() {
        return login;
    }
}
