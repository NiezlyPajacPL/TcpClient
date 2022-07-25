package main.scenes.clientScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.network.Client;
import main.network.TcpClient;

import java.io.IOException;

public class ClientScene {

    Client client;

    public ClientScene(Client client){
        this.client = client;
    }

    public void display(){
        FXMLLoader fxmlLoader = new FXMLLoader(ClientScene.class.getResource("/Scenes/main-view.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("client");
        window.setMinWidth(250);

        try {
            Scene scene = new Scene(fxmlLoader.load());
            ClientSceneController clientSceneController = fxmlLoader.getController();
            clientSceneController.construct((TcpClient) client);

            window.setScene(scene);
            window.show();

        } catch (IOException e) {
           e.printStackTrace();
        }
    }

}
