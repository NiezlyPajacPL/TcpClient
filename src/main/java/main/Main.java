package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.helpers.MessageData;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.managers.SoundHandler;
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


public class Main extends Application {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
    FXMLLoader clientLoader = new FXMLLoader(ClientScene.class.getResource("/Scenes/main-view.fxml"));
    LoginController loginController;
    static ClientSceneController clientSceneController;
    static Client client;
    ClientScene clientScene;
   // Stage clientWindow = new Stage();
    private static final Map<String, MessagingTab> openTabs = new HashMap<>();


    public static void main(String[] args) {
        final String CONNECTION_IP = "109.207.149.50";
        final int CONNECTION_PORT = 4446;
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(MessageData messageData) {
         //       String sender = getSenderFromString(message);

                if (openTabs.get(messageData.getSender()) == null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            TextArea textArea = new TextArea();
                            Tab userTab = TabCreator.createTab(messageData.getSender(), textArea);
                            userTab.setOnClosed(event -> openTabs.remove(messageData.getSender()));
                            clientSceneController.tabPane.getTabs().add(userTab);
                            openTabs.put(messageData.getSender(), new MessagingTab(userTab, textArea));
                            openTabs.get(messageData.getSender()).getTextArea().appendText(messageData.getMessage() + "\n");
                            SoundHandler.playSound(SoundHandler.MESSAGE_INBOUND);
                        }
                    });
                } else {
                    SoundHandler.playSound(SoundHandler.MESSAGE_IN_OPENED_TAB);
                    openTabs.get(messageData.getSender()).getTextArea().appendText(messageData.getMessage() + "\n");
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
                            clientScene = new ClientScene(client, clientLoader, clientSceneController, userName, openTabs);
                            clientScene.display();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                SoundHandler.playSound(SoundHandler.CONNECTED);
            }
        };
        loginController.construct(client, loginListener);

        stage.setMinWidth(380);
        stage.setMinHeight(380);
        stage.setTitle("PogChat");
        stage.setScene(scene);
        stage.show();

    }

}
