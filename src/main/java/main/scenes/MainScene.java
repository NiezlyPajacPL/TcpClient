package main.scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.scenes.controllers.MainSceneController;

public class MainScene extends Application implements Runnable{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/todoname-view.fxml"));
        VBox vBox = fxmlLoader.load();
        MainSceneController mainSceneController = fxmlLoader.getController();
        Scene scene = new Scene(vBox, 320, 320);
        stage.setTitle("asd");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void run() {

    }
}
