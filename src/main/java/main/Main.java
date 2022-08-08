package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.managers.SoundHandler;
import main.managers.SubtitlesPrinter;
import main.managers.settings.Settings;
import main.messageTypes.Message;
import main.network.Client;
import main.network.MessageListener;
import main.network.TcpClient;
import main.scenes.ClientScene;
import main.controllers.LoginController;
import main.scenes.LoginListener;
import main.scenes.LoginScene;

import java.io.File;
import java.io.IOException;


public class Main extends Application {
    private final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/login-view.fxml"));
    private final FXMLLoader clientLoader = new FXMLLoader(ClientScene.class.getResource("/scenes/main-view.fxml"));
    private static final String settingsFilePath = "src/main/resources/settings/settings.txt";
    private LoginController loginController;
    private static ClientSceneController clientSceneController;
    private static Client client;
    private ClientScene clientScene;
    private static final Settings settings = new Settings(settingsFilePath);
    private final Image applicationIcon = new Image(new File("src/main/resources/icon.png").toURI().toString());
    private final String applicationTitle = "PogChat";

    public static void main(String[] args) {
        final String CONNECTION_IP = settings.getConnectionIP();
        final int CONNECTION_PORT = settings.getConnectionPort();
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
                            clientSceneController.construct((TcpClient) client, userName,settings);

                            clientScene = new ClientScene(clientLoader,applicationIcon,applicationTitle);
                            clientScene.display();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (!settings.isSoundMuted()) {
                    SoundHandler.playSound(SoundHandler.CONNECTED);
                }
            }
        };

        loginController.construct((TcpClient) client, loginListener);
        stage.setMinWidth(380);
        stage.setMinHeight(380);
        stage.setTitle(applicationTitle);
        stage.getIcons().add(applicationIcon);
        stage.setScene(scene);
        stage.show();
    }
}
