package main.scenes.clientScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.scenes.login.ClientSceneController;

import java.io.IOException;

public class ClientScene {

    public static void display(){
        FXMLLoader fxmlLoader = new FXMLLoader(ClientScene.class.getResource("/Scenes/main-view.fxml"));
        ClientSceneController clientSceneController = fxmlLoader.getController();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("client");
        window.setMinWidth(250);
        try {
            Scene scene = new Scene(fxmlLoader.load());
            window.setScene(scene);
            window.show();

        } catch (IOException e) {
           e.printStackTrace();
        }
    }

}