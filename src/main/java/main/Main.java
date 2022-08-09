package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.managers.SoundHandler;
import main.managers.console.ConsolePrinter;
import main.managers.settings.Settings;
import main.network.Client;
import main.network.TcpClient;
import main.scenes.ClientScene;
import main.controllers.LoginController;
import main.scenes.LoginListener;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private static Client client;
    //SCENES
    private final FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/scenes/login-view.fxml"));
    private final FXMLLoader clientLoader = new FXMLLoader(ClientScene.class.getResource("/scenes/main-view.fxml"));
    private LoginController loginController;
    private static ClientSceneController clientSceneController;
    private ClientScene clientScene;
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
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(loginLoader.load());
        loginController = loginLoader.getController();
        loginController.construct((TcpClient) client, new LoginListener() {
            @Override
            public void onClientLoggedIn() {
                String userName = loginController.getUserName();
                Platform.runLater(() -> {
                    stage.close();
                    try {
                        clientLoader.load();
                        clientSceneController = clientLoader.getController();
                        clientSceneController.construct((TcpClient) client, userName, settings);

                        clientScene = new ClientScene(clientLoader,applicationIcon,applicationTitle);
                        clientScene.display();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                if (!settings.isSoundMuted()) {
                    SoundHandler.playSound(SoundHandler.CONNECTED);
                }
            }
        });

        stage.setMinWidth(380);
        stage.setMinHeight(380);
        stage.setTitle(applicationTitle);
        stage.getIcons().add(applicationIcon);
        stage.setScene(scene);
        stage.show();
    }
}
