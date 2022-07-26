package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.network.TcpClient;
import org.w3c.dom.events.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientSceneController {

    private TcpClient client;
    private ArrayList<String> usersList;
    String[] usersString;

    @FXML
    Button refresh;
    @FXML
    ListView<String> usersListView;

    @FXML
    protected void onRefresh() {
        loadUsersList();
        usersString = convertArrayList();
        usersListView.getItems().clear();
        usersListView.getItems().addAll(usersString);
    }

    public void construct(TcpClient client) {
        this.client = client;
        loadUsersList();
    }

    @FXML
    public void handleMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.println("clicked on " + usersListView.getSelectionModel().getSelectedItem());
    }

    private void loadUsersList() {
        client.sendMessage("/allUsers");
        usersList = client.getUsersList();
    }

    private String[] convertArrayList(){
        String[] userStrings = new String[usersList.size()];
        for(int i = 0; i < usersList.size();i++){
            userStrings[i] = usersList.get(i).replace("[", "").replace("]", "").replace(",","");
            System.out.println(userStrings[i]);
        }
        return userStrings;
    }

}
