package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.managers.SoundHandler;
import main.managers.console.ConsolePrinter;
import main.managers.settings.Settings;
import main.network.Client;
import main.network.TcpClient;
import main.controllers.LoginController;
import main.helpers.Listeners.LoginListener;

import java.io.File;

public class Main extends Application {
    private static Client client;
    //SCENES
    private final FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/scenes/login-view.fxml"));
    private final FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("/scenes/main-view.fxml"));
    private LoginController loginController;
    private static ClientSceneController clientSceneController;

    //SETTINGS
    private static final String settingsFilePath = "src/main/resources/settings/settings.txt";
    private static final Settings settings = new Settings(settingsFilePath);
    //IMAGE AND TITLE
    private final Image applicationIcon = new Image(new File("src/main/resources/icon.png").toURI().toString());
    private final String applicationTitle = "PogChat";

    public static void main(String[] args) {
        final String CONNECTION_IP = settings.getConnectionIP();
        final int CONNECTION_PORT = settings.getConnectionPort();
        client = new TcpClient(CONNECTION_IP, CONNECTION_PORT);
        Thread clientThread = new Thread(client);
        clientThread.start();
        ConsolePrinter.printRegistrationRequest();
        ConsolePrinter.printIsHelpNeeded();

        launch();
        System.exit(0);
    }

    @Override
    public void start(Stage loginWindow) throws Exception {
        Stage clientWindow = new Stage();
        Scene loginScene = new Scene(loginLoader.load());
        Scene clientScene = new Scene(clientLoader.load());

        loginController = loginLoader.getController();
        loginController.construct((TcpClient) client, new LoginListener() {
            @Override
            public void onClientLoggedIn() {
                String userName = loginController.getUserName();
                Platform.runLater(() -> {
                    loginWindow.close();
                    clientSceneController = clientLoader.getController();
                    clientSceneController.construct((TcpClient) client, userName, settings);
                    displayClientWindow(clientWindow,clientScene);
                });
                if (!settings.isSoundMuted()) {
                    SoundHandler.playSound(SoundHandler.CONNECTED);
                }
            }
        });
        displayLoginWindow(loginWindow,loginScene);
    }

    private void displayClientWindow(Stage clientWindow, Scene clientScene){
        clientWindow.initModality(Modality.APPLICATION_MODAL);
        clientWindow.setTitle(applicationTitle);
        clientWindow.getIcons().add(applicationIcon);
        clientWindow.setMinWidth(600);
        clientWindow.setMinHeight(450);
        clientWindow.setMaxWidth(640);
        clientWindow.setMaxHeight(500);
        clientWindow.setScene(clientScene);
        clientWindow.show();
    }

    private void displayLoginWindow(Stage loginWindow,Scene loginScene){
        loginWindow.setMinWidth(380);
        loginWindow.setMinHeight(380);
        loginWindow.setTitle(applicationTitle);
        loginWindow.getIcons().add(applicationIcon);
        loginWindow.setScene(loginScene);
        loginWindow.show();
    }
}
