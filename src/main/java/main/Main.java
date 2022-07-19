package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        VBox vBox = fxmlLoader.load();
        Button btn = new Button();
        loginController = fxmlLoader.getController();
        btn.setText("Quit");
        btn.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        vBox.getChildren().add(btn);
        Scene scene = new Scene(vBox, 320, 320);
        stage.setTitle("asd");
        stage.setScene(scene);
        stage.show();

        LoginHandler loginHandler = new LoginHandler(fxmlLoader,loginController,client,clientName);
        Thread thread = new Thread(loginHandler);
        thread.start();

    }
}
