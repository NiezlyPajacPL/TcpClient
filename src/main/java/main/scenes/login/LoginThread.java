package main.scenes.login;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import main.controllers.LoggingInController;
import main.network.Client;

import static java.lang.Thread.sleep;

public class LoginThread implements Runnable {

    FXMLLoader fxmlLoader;
    LoggingInController loggingInController;
    Client client;
    LoginListener loginListener;

    public LoginThread(FXMLLoader fxmlLoader, LoggingInController loggingInController, Client client, LoginListener loginListener) {
        this.fxmlLoader = fxmlLoader;
        this.loggingInController = loggingInController;
        this.client = client;
        this.loginListener = loginListener;
    }


    @Override
    public void run() {
       while (true) {
            loggingInController = fxmlLoader.getController();
            if (!client.isClientLoggedIn()) {
                if (loggingInController.getLogin() != null) {
                    client.sendMessage(loggingInController.getLoginCommand());
                    System.out.println("Log: Login request sent");
                    loggingInController.setLoginToNull();
                    Platform.runLater(() -> loggingInController.getSomethingWentWrongLabel().setText("Wrong username or password."));
                }
            } else {
                System.out.println("Log: Client logged in, closing login thread.");
                loginListener.onClientLoggedIn();
                break;
            }
        }
    }
}

/*    private boolean clientConnected() {
        loginController = fxmlLoader.getController();
        if (client == null) {
            Platform.runLater(() -> loginController.connectionProblems.setText("Connection problems... Trying to reconnect."));
            return false;
        }
        Platform.runLater(() -> loginController.connectionProblems.setText(""));
        return true;
    }*/

/*        while (true){
            if(client.isClientLoggedIn()){
             //   System.out.println("asd");
                System.out.println("Log: Client logged in, closing login thread.");
                loginListener.onClientLoggedIn();
                break;
            }
        }*/