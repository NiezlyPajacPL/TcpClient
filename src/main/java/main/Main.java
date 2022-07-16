package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.managers.SubtitlesPrinter;
import main.network.Client;
import main.network.InputReader;
import main.network.TcpClient;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login-view.fxml"))), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        final String CONNECTION_IP = "127.0.0.1";
        final int CONNECTION_PORT = 4445;

/*        Scanner scan = new Scanner(System.in);
        Client client = new TcpClient(CONNECTION_IP, CONNECTION_PORT);
        Thread clientThread = new Thread(client);
        clientThread.start();
        SubtitlesPrinter.printRegistrationRequest();
        SubtitlesPrinter.printIsHelpNeeded();

        InputReader inputReader = new InputReader(scan, client);
        inputReader.start();
        */
    }
}
