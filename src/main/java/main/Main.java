package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.managers.SubtitlesPrinter;
import main.network.Client;
import main.network.MessageListener;
import main.network.TcpClient;
import main.scenes.clientScene.ClientScene;
import main.controllers.LoginController;
import main.scenes.login.LoginListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main extends Application {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
    static FXMLLoader clientLoader = new FXMLLoader(ClientScene.class.getResource("/Scenes/main-view.fxml"));
    LoginController loginController;
    static ClientSceneController clientSceneController;
    static Client client;
    Stage clientWindow = new Stage();
    private static final Map<String, MessagingTab> openTabs = new HashMap<>();


    public static void main(String[] args) {
        final String CONNECTION_IP = "127.0.0.1";
        final int CONNECTION_PORT = 4445;
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(String message) {
                String sender = getSenderFromString(message);

                if (openTabs.get(sender) == null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            TextArea textArea = new TextArea();
                            Tab userTab = TabCreator.createTab(sender, textArea);
                            userTab.setOnClosed(event -> openTabs.remove(sender));
                            clientSceneController.tabPane.getTabs().add(userTab);
                            openTabs.put(sender, new MessagingTab(userTab, textArea));
                            openTabs.get(sender).getTextArea().appendText(message + "\n");
                        }
                    });
                } else {
                    openTabs.get(sender).getTextArea().appendText(message + "\n");
                }

            }
        };
        //  Scanner scan = new Scanner(System.in);
        client = new TcpClient(CONNECTION_IP, CONNECTION_PORT, messageListener);
        Thread clientThread = new Thread(client);
        clientThread.start();
        SubtitlesPrinter.printRegistrationRequest();
        SubtitlesPrinter.printIsHelpNeeded();

        launch();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(fxmlLoader.load());
        loginController = fxmlLoader.getController();
        LoginListener loginListener = new LoginListener() {
            @Override
            public void onClientLoggedIn() {
                String userName = loginController.getUserName();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.close();
                        try {
                            clientLoader.load();
                            clientSceneController = clientLoader.getController();
                            ClientScene clientScene = new ClientScene(client, clientLoader, clientSceneController, userName, clientWindow, openTabs);
                            clientScene.display();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        loginController.construct(client, loginListener);

        stage.setMinWidth(350);
        stage.setMinHeight(380);
        stage.setTitle("PogChat");
        stage.setScene(scene);
        stage.show();

    }

    private static String getSenderFromString(String string) {
        String[] sender = string.split(" ");
        return sender[0].replace(":", "");
    }
}
