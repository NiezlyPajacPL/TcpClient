package main.scenes.login;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import main.network.Client;

public class LoginThread implements Runnable {

    FXMLLoader fxmlLoader;
    LoginController loginController;
    Client client;
    LoginListener loginListener;

    public LoginThread(FXMLLoader fxmlLoader, LoginController loginController, Client client, LoginListener loginListener) {
        this.fxmlLoader = fxmlLoader;
        this.loginController = loginController;
        this.client = client;
        this.loginListener = loginListener;
    }


    @Override
    public void run() {
        while (true) {
            if (client != null) {
                loginController = fxmlLoader.getController();
                if (!client.isClientLoggedIn()) {
                    if (loginController.login != null) {
                        client.sendMessage(loginController.getCommand());
                        System.out.println("Log: Login request sent");
                        loginController.login = null;
                        Platform.runLater(() -> loginController.wrongPassword.setText("Wrong username or password."));
                    }
                } else {
                    System.out.println("Log: Client logged in, closing login thread.");
                    loginListener.onClientLoggedIn();
                    break;
                }
            }else {
                Platform.runLater(() -> loginController.connectionProblems.setText("Connection problems... Trying to reconnect."));
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
}