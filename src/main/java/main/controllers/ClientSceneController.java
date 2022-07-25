package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.network.Client;
import main.network.TcpClient;

public class ClientSceneController {

    public TcpClient client;

    @FXML
    Button refresh;
    @FXML
    ListView<String> usersList;
    @FXML
    protected void  onRefresh(){
       client.sendMessage("/allUsers");
    }

    public void construct(TcpClient client){
        this.client = client;
    }
}
