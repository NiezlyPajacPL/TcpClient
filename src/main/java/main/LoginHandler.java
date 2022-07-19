package main;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import main.network.Client;

import static java.lang.Thread.sleep;

public class LoginHandler implements Runnable {

    FXMLLoader fxmlLoader;
    LoginController loginController;
    Client client;
    String clientName;

    LoginHandler(FXMLLoader fxmlLoader, LoginController loginController, Client client, String clientName) {
        this.fxmlLoader = fxmlLoader;
        this.loginController = loginController;
        this.client = client;
        this.clientName = clientName;
    }


    @Override
    public void run() {
        while (true) {
            loginController = fxmlLoader.getController();
            if (!client.isClientLoggedIn()) {
                if (loginController.login != null) {
                    client.sendMessage(loginController.getCommand());
                    loginController.login = null;
                    if(client.isClientLoggedIn()){
                        Platform.exit();
                        break;
                    }
                }
            }
        }
    }
}