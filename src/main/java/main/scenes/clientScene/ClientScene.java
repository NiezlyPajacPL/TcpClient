package main.scenes.clientScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.helpers.MessagingTab;
import main.managers.settings.Settings;
import main.network.Client;
import main.network.TcpClient;

import java.io.File;
import java.util.Map;

public class ClientScene {

    private final Client client;
    private final String userName;
    private final FXMLLoader fxmlLoader;
    private final ClientSceneController clientSceneController;
    private final Settings settings;
    private final File icon;

    public ClientScene(Client client, FXMLLoader fxmlLoader, ClientSceneController clientSceneController, String userName,Settings settings,File icon) {
        this.client = client;
        this.fxmlLoader = fxmlLoader;
        this.userName = userName;
        this.clientSceneController = clientSceneController;
        this.settings = settings;
        this.icon = icon;
    }

    public void display() {
        Stage window = new Stage();
        Image image = new Image(icon.toURI().toString());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("PogChat");
        window.getIcons().add(image);
        window.setMinWidth(600);
        window.setMinHeight(450);
        window.setMaxWidth(640);
        window.setMaxHeight(500);

        Scene scene = new Scene(fxmlLoader.getRoot());
        clientSceneController.construct((TcpClient) client, userName,settings);

        window.setScene(scene);
        window.show();
    }

    public ClientSceneController getClientSceneController() {
        return clientSceneController;
    }
}
