package main.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;

public class ClientScene {
    private final FXMLLoader fxmlLoader;
    private final File icon;
    private final String title;

    public ClientScene(FXMLLoader fxmlLoader,File icon,String title) {
        this.fxmlLoader = fxmlLoader;
        this.icon = icon;
        this.title = title;
    }

    public void display() {
        Stage window = new Stage();
        Image image = new Image(icon.toURI().toString());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
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
