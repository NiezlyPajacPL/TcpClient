package main;

import javafx.fxml.FXMLLoader;
import main.network.Client;

import static java.lang.Thread.sleep;

public class InputHandler implements Runnable{

    FXMLLoader fxmlLoader;
    LoginController loginController;
    Client client;

    InputHandler(FXMLLoader fxmlLoader,LoginController loginController,Client client){
        this.fxmlLoader = fxmlLoader;
        this.loginController = loginController;
        this.client = client;
    }


    @Override
    public void run() {
        while (true) {
            loginController = fxmlLoader.getController();

            if(loginController.login!=null){
                System.out.println(loginController.login);
                client.sendMessage(loginController.getCommand());
                break;
            }
        }
    }
}