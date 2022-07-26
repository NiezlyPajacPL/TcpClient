package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.managers.SubtitlesPrinter;
import main.network.Client;
import main.network.TcpClient;
import main.scenes.clientScene.ClientScene;
import main.controllers.LoginController;
import main.scenes.login.LoginListener;

import java.util.Scanner;



public class Main extends Application {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
    LoginController loginController;
    static Client client;

    public static void main(String[] args) {
        final String CONNECTION_IP = "127.0.0.1";
        final int CONNECTION_PORT = 4445;
        Scanner scan = new Scanner(System.in);
        client = new TcpClient(CONNECTION_IP, CONNECTION_PORT);
        Thread clientThread = new Thread(client);
        clientThread.start();
        SubtitlesPrinter.printRegistrationRequest();
        SubtitlesPrinter.printIsHelpNeeded();

        launch();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LoginListener loginListener = new LoginListener() {
            @Override
            public void onClientLoggedIn() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.close();
                        ClientScene clientScene = new ClientScene(client);
                        clientScene.display();
                    }
                });
            }
        };
        Scene scene = new Scene(fxmlLoader.load());
        loginController = fxmlLoader.getController();
        loginController.construct(client,loginListener);

        stage.setMinWidth(350);
        stage.setMinHeight(380);
        stage.setTitle("PogChat");
        stage.setScene(scene);
        stage.show();

    }

}
