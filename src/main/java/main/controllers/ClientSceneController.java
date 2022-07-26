package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.helpers.MessagingTab;
import main.network.TcpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientSceneController {

    private TcpClient client;
    private ArrayList<String> usersList;
    private final Map<String, MessagingTab> openTabs = new HashMap<>();
    private String userName;

    public void construct(TcpClient client,String userName) {
        this.client = client;
        this.userName = userName;
        loadUsersList();
    }

    @FXML
    Button refresh;
    @FXML
    ListView<String> usersListView;
    @FXML
    TabPane tabPane;
    @FXML
    TextArea messageTextArea;

    @FXML
    protected void onRefresh() {
        loadUsersList();
        String[] usersString = convertArrayList();
        usersListView.getItems().clear();
        usersListView.getItems().addAll(usersString);
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
    protected void onSendButtonClicked(){
        String receiver = getUserFromOpenedTab();
        String message = messageTextArea.getText();
        if(receiver != null && message != null){
            client.sendMessage(messageCommand(receiver,message));
            openTabs.get(receiver).getTextArea().appendText(userName + ": " + message + "\n");
        }
    }

    private void loadUsersList() {
        client.sendMessage("/allUsers");
        usersList = client.getUsersList();
    }

    private String[] convertArrayList(){
        String[] userStrings = new String[usersList.size()];
        for(int i = 0; i < usersList.size();i++){
            userStrings[i] = usersList.get(i).replace("[", "").replace("]", "").replace(",","");
        }
        return userStrings;
    }

    private String getUserFromOpenedTab(){
        for(Map.Entry<String,MessagingTab> entry : openTabs.entrySet()){
            if(entry.getValue().getTab().isSelected()){
                return entry.getKey();
            }
        }
        return null;
    }

    private String messageCommand(String receiver, String message){
        return "/msg " + receiver + " " + message;
    }
}
