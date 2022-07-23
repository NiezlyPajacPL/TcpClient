package main.scenes.login;

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
            loginController = fxmlLoader.getController();

            if (!client.isClientLoggedIn()) {
                if (loginController.login != null) {
                    client.sendMessage(loginController.getCommand());
                    System.out.println("Login request sent");
                    loginController.login = null;
                }
            }else{
                System.out.println("closing thread");
                loginListener.onClientLoggedIn();
                break;
            }
        }
    }

}