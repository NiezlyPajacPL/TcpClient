package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.managers.SubtitlesPrinter;
import main.network.Client;
import main.network.TcpClient;
import main.scenes.MainScene;

import java.util.Scanner;



public class Main extends Application {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
    LoginController loginController;
    static Client client;
    String clientName;

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
    }

    @Override
    public void start(Stage stage) throws Exception {
        loginController = fxmlLoader.getController();
        System.out.println(clientName);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinWidth(320);
        stage.setMinHeight(380);
        stage.setTitle("asd");
        stage.setScene(scene);
        stage.show();

        LoginListener loginListener = new LoginListener() {
            @Override
            public void onClientLoggedIn() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                        MainScene mainScene = new MainScene();
                        mainScene.display();
                    }
                });
            }
        };
        LoginThread loginHandler = new LoginThread(fxmlLoader, loginController, client, loginListener);
        Thread thread = new Thread(loginHandler);
        thread.start();
    }

}
