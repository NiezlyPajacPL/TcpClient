package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.managers.Logger;
import main.network.TcpClient;
import main.scenes.login.LoginListener;

import static java.lang.Thread.sleep;


public class LoginController {

    private String login;
    private String password;
    private final String LOGIN = "Log in!";
    private final String REGISTER = "Register!";
    private final String LOGIN_COMMAND = "/login ";
    private final String REGISTER_COMMAND = "/register ";
    private final String WRONG_PASSWORD = "Wrong username or password.";
    private final String LOGIN_TOO_LONG = "Login can't exceed 20 characters";
    private final String LOST_CONNECTION = "Connection has been lost. Trying to reconnect..";
    private final String RECONNECTED = "Successfully reconnected.";
    private final String LOGGING_IN = "Logging in..";
    private String loginType = LOGIN;

    public TcpClient client;
    public LoginListener loginListener;

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

    public void construct(TcpClient client, LoginListener loginListener) {
        this.client = client;
        this.loginListener = loginListener;
    }

    @FXML
    protected void onButtonClick() {
        if (!loginButton.getText().equals(LOGGING_IN)) {
            login = loginField.getText();
            password = passwordField.getText();

            if (login != null && !login.equals("") && password != null && !password.equals("")) {
                if (client.isClientConnected()) {
                    loginButton.setText(LOGGING_IN);
                    if (loginType.equals(LOGIN)) {
                        client.sendMessage(getLoginCommand());
                    } else if (loginType.equals(REGISTER)) {
                        client.sendMessage(getRegisterCommand());
                    }
                }
                Thread thread = new Thread(() -> {
                    while (true) {
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Logger.loginAttempt();
                        if (client.messageArrived) {
                            client.messageArrived = false;
                            break;
                        }
                    }
                });
                Thread loginThread = new Thread(() -> {
                    if (client.isClientConnected()) {
                        try {
                            thread.start();
                            thread.join();
                            if (client.isClientLoggedIn()) {
                                onSuccessfullyLogged();
                            } else if (!client.isClientLoggedIn()) {
                                onWrongPassword();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        onConnectionLost();
                    }
                });
                loginThread.start();
  /*          Delay.delay(2000, new Runnable() {
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
            });*/
            }else if(login != null && login.length() < 20){
                Platform.runLater(() -> {
                    somethingWentWrong.setText(LOGIN_TOO_LONG);
                });
            }
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
        return LOGIN_COMMAND + login + " " + password;
    }

    private String getRegisterCommand() {
        return REGISTER_COMMAND + login + " " + password;
    }

    private void onSuccessfullyLogged() {
        Logger.loggedIn();
        loginListener.onClientLoggedIn();
        Platform.runLater(() -> {
            passwordField.setText("");
            loginButton.setText(loginType);
        });
    }

    private void onWrongPassword() {
        Platform.runLater(() -> {
            if (!somethingWentWrong.getText().equals(WRONG_PASSWORD)) {
                somethingWentWrong.setText(WRONG_PASSWORD);
            }
            passwordField.setText("");
            loginButton.setText(loginType);
        });
    }

    private void onConnectionLost() {
        Platform.runLater(() -> {
            if (!somethingWentWrong.getText().equals(LOST_CONNECTION)) {
                somethingWentWrong.setText(LOST_CONNECTION);
            }
        });
    }

/*    private void onConnectionRenewed() {
        somethingWentWrong.setText(RECONNECTED);
        somethingWentWrong.setTextFill(GREEN);
    }*/
}
