package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.network.TcpClient;
import main.scenes.login.LoginListener;

import static java.lang.Thread.sleep;


public class LoginController {

    private String login;
    private String password;
    private final String LOGIN = "Log in!";
    private final String REGISTER = "Register!";
    final String WRONG_PASSWORD = "Wrong username or password.";
    final String LOST_CONNECTION = "Connection has been lost. Trying to reconnect..";


    private String loginType = LOGIN;

    public TcpClient client;
    public LoginListener loginListener;

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

    public void construct(TcpClient client, LoginListener loginListener) {
        this.client = client;
        this.loginListener = loginListener;
    }

    @FXML
    protected void onButtonClick() {
        if (!loginButton.getText().equals("Logging in...")) {

            login = loginField.getText();
            password = passwordField.getText();

            if (login!= null && !login.equals("") && password != null && !password.equals("")) {
                if (client.isClientConnected()) {
                    loginButton.setText("Logging in..");
                    if (loginType.equals(LOGIN)) {
                        client.sendMessage(getLoginCommand());
                    } else if (loginType.equals(REGISTER)) {
                        client.sendMessage(getRegisterCommand());
                    }
                }
                Thread temp = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Attempting to log in");
                            if (client.messageArrived) {
                                System.out.println("closing temp");
                                client.messageArrived = false;
                                break;
                            }
                        }
                    }
                });

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            temp.start();
                            temp.join();
                            if (client.isClientLoggedIn()) {
                                onSuccessfullyLogged();
                            } else {
                                onWrongPassword();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

/*
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        if(!temp.isAlive()){
                            if(client.isClientLoggedIn()){
                                System.out.println("tak");
                                onSuccessfullyLogged();
                            }else {
                                System.out.println("nie");
                                onWrongPassword();
                            }
                            break;
                        }
                    }
                }
            });
*/

                thread.start();
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
        Platform.runLater(() -> {
            if (!somethingWentWrong.getText().equals(WRONG_PASSWORD)) {
                somethingWentWrong.setText(WRONG_PASSWORD);
            }
            passwordField.setText("");
            loginButton.setText(loginType);
        });
    }

    private void onConnectionLost() {
        if (!somethingWentWrong.getText().equals(LOST_CONNECTION)) {
            somethingWentWrong.setText(LOST_CONNECTION);
        }
    }
}
