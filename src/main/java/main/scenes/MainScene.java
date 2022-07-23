package main.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScene{


    public void display(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("/Scenes/main-view.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TEMP");
        window.setMinWidth(250);
        try {
            Scene scene = new Scene(fxmlLoader.load());
            window.setScene(scene);
            window.show();

        } catch (IOException e) {
            System.out.println("nie dziala xd");
        }
    }

    public static void close(){

    }
}
