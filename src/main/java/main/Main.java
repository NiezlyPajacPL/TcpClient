package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.managers.SubtitlesPrinter;
import main.network.Client;
import main.network.TcpClient;

import java.util.Scanner;


public class Main extends Application{
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
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox vBox = fxmlLoader.load();
        loginController = fxmlLoader.getController();
        Scene scene = new Scene(vBox, 320, 320);
        stage.setTitle("rafauchamp");
        stage.setScene(scene);
        stage.show();

        InputHandler inputHandler = new InputHandler(fxmlLoader,loginController,client);
        Thread thread = new Thread(inputHandler);
        thread.start();

    }
}
