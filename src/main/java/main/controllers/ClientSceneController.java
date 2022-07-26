package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import main.helpers.MessagingTab;
import main.network.TcpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientSceneController {

    private TcpClient client;
    private ArrayList<String> usersList;
    private final Map<String, MessagingTab> openTabs = new HashMap<>();

    @FXML
    Button refresh;
    @FXML
    ListView<String> usersListView;
    @FXML
    TabPane tabPane;
    @FXML
    TextField messageTextField;

    @FXML
    protected void onRefresh() {
        loadUsersList();
        String[] usersString = convertArrayList();
        usersListView.getItems().clear();
        usersListView.getItems().addAll(usersString);
    }

    public void construct(TcpClient client) {
        this.client = client;
        loadUsersList();
    }

    @FXML
    protected void handleMouseClick() {
        String user = usersListView.getSelectionModel().getSelectedItem();
        System.out.println("clicked on " + user );

        if(user!= null && !user.equals("") && openTabs.get(user) == null){
            Tab userTab = new Tab();
            userTab.setText(user);
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            userTab.setContent(textArea);
            userTab.setOnClosed(event -> openTabs.remove(user));
            tabPane.getTabs().add(userTab);
            openTabs.put(user,new MessagingTab(userTab,textArea));
        }
    }

    @FXML
    protected void sendMessage(KeyEvent enter){

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
