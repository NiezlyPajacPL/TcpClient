package main.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class LoginScene {
    private final FXMLLoader fxmlLoader;
    private final File icon;
    private final String applicationTitle;

    public LoginScene(FXMLLoader fxmlLoader,File icon,String applicationTitle) {
        this.fxmlLoader = fxmlLoader;
        this.icon = icon;
        this.applicationTitle = applicationTitle;
    }

    public void display() {
        Stage window = new Stage();
        Image image = new Image(icon.toURI().toString());
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(applicationTitle);
        window.getIcons().add(image);
        window.setMinWidth(380);
        window.setMinHeight(380);
        Scene scene = new Scene(fxmlLoader.getRoot());
        window.setScene(scene);
        window.show();
    }
}
