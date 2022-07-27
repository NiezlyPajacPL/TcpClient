package main.scenes.clientScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.helpers.MessagingTab;
import main.network.Client;
import main.network.TcpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientScene {

    Client client;
    String userName;
    Stage window;
    private Map<String, MessagingTab> openTabs;
    FXMLLoader fxmlLoader;
    ClientSceneController clientSceneController;
    public ClientScene(Client client,FXMLLoader fxmlLoader,ClientSceneController clientSceneController,String userName,Stage window,Map<String, MessagingTab> openTabs){
        this.client = client;
        this.fxmlLoader = fxmlLoader;
        this.userName = userName;
        this.window = window;
        this.openTabs = openTabs;
        this.clientSceneController = clientSceneController;
    }

    public void display(){
      //  FXMLLoader fxmlLoader = new FXMLLoader(ClientScene.class.getResource("/Scenes/main-view.fxml"));
       // Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("client");
        window.setMinWidth(250);

        Scene scene = new Scene(fxmlLoader.getRoot());
        //clientSceneController = fxmlLoader.getController();
        clientSceneController.construct((TcpClient) client,userName,openTabs);

        window.setScene(scene);
        window.show();

    }

    public ClientSceneController getClientSceneController() {
        return clientSceneController;
    }
}
