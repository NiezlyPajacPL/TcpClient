package main.scenes.clientScene;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.ClientSceneController;
import main.helpers.MessagingTab;
import main.network.Client;
import main.network.TcpClient;
import java.util.Map;

public class ClientScene {

    Client client;
    String userName;
    private Map<String, MessagingTab> openTabs;
    FXMLLoader fxmlLoader;
    ClientSceneController clientSceneController;

    public ClientScene(Client client,FXMLLoader fxmlLoader,ClientSceneController clientSceneController,String userName,Map<String, MessagingTab> openTabs){
        this.client = client;
        this.fxmlLoader = fxmlLoader;
        this.userName = userName;
        this.openTabs = openTabs;
        this.clientSceneController = clientSceneController;
    }

    public void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("PogChat");
        window.setMinWidth(600);
        window.setMinHeight(450);
        window.setMaxWidth(640);
        window.setMaxHeight(500);

        Scene scene = new Scene(fxmlLoader.getRoot());
        clientSceneController.construct((TcpClient) client,userName,openTabs);

        window.setScene(scene);
        window.show();

    }
    public ClientSceneController getClientSceneController() {
        return clientSceneController;
    }
}
