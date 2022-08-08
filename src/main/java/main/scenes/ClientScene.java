package main.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientScene {
    private final FXMLLoader fxmlLoader;
    private final Image applicationIcon;
    private final String applicationTitle;

    public ClientScene(FXMLLoader fxmlLoader,Image applicationIcon,String applicationTitle) {
        this.fxmlLoader = fxmlLoader;
        this.applicationIcon = applicationIcon;
        this.applicationTitle = applicationTitle;
    }

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(applicationTitle);
        window.getIcons().add(applicationIcon);
        window.setMinWidth(600);
        window.setMinHeight(450);
        window.setMaxWidth(640);
        window.setMaxHeight(500);

        Scene scene = new Scene(fxmlLoader.getRoot());
        window.setScene(scene);
        window.show();
    }
}
