package main.scenes.login;

import main.network.Client;


public class LoginThread implements Runnable {

    Client client;
    LoginListener loginListener;
    WrongPasswordListener wrongPasswordListener;

    public LoginThread(Client client, LoginListener loginListener, WrongPasswordListener wrongPasswordListener){
        this.client =client;
        this.loginListener = loginListener;
        this.wrongPasswordListener = wrongPasswordListener;
    }

    @Override
    public void run() {
        while (true){
            if(!client.isClientLoggedIn()){
                wrongPasswordListener.onWrongPassword();
            }else if(client.isClientLoggedIn()){
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

/*       while (true) {
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
        }*/