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
    private final FXMLLoader fxmlLoader;
    private final File icon;

    public ClientScene(FXMLLoader fxmlLoader,File icon) {
        this.fxmlLoader = fxmlLoader;
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
        window.setScene(scene);
        window.show();
    }
}
