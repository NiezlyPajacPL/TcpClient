package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.network.TcpClient;

import java.util.*;

public class ClientSceneController {

    private TcpClient client;
    private ObservableList<String> observableList;
    private Map<String, MessagingTab> openTabs;
    private String userName;
    FilteredList<String> userFilter;

    public void construct(TcpClient client, String userName, Map<String, MessagingTab> openTabs) {
        this.client = client;
        this.userName = userName;
        this.openTabs = openTabs;
        loadUsersList();
        startSearchFilter();
    }

    @FXML
    Button refresh;
    @FXML
    ListView<String> usersListView;
    @FXML
    public TabPane tabPane;
    @FXML
    TextArea messageTextArea;
    @FXML
    TextField searchField;

    @FXML
    protected void onRefresh() {
        usersListView.getItems().clear();
        loadUsersList();

        usersListView.getItems().addAll(userFilter);
    }

    @FXML
    protected void handleMouseClick() {
        String user = usersListView.getSelectionModel().getSelectedItem();
        System.out.println("clicked on " + user);

        if (user != null && !user.equals("") && openTabs.get(user) == null) {
            TextArea textArea = new TextArea();
            Tab userTab = TabCreator.createTab(user, textArea);

            userTab.setOnClosed(event -> openTabs.remove(user));
            tabPane.getTabs().add(userTab);
            openTabs.put(user, new MessagingTab(userTab, textArea));
        }
    }

    @FXML
    protected void onSendButtonClicked() {
        String receiver = getUserFromOpenedTab();
        String message = messageTextArea.getText();
        if (receiver != null && message != null) {
            client.sendMessage(messageCommand(receiver, message));
            openTabs.get(receiver).getTextArea().appendText(userName + ": " + message + "\n");
            messageTextArea.clear();
        }
    }

    private void startSearchFilter(){
        userFilter = new FilteredList<>(observableList,s -> true);

        searchField.textProperty().addListener(observable -> {
            String filter = searchField.getText();

            if(filter== null || filter.length() == 0){
                userFilter.setPredicate(s -> true);
            }else{
                userFilter.setPredicate(s -> s.contains(filter));
                usersListView.getItems().clear();
                usersListView.getItems().addAll(userFilter);
            }
        });
    }

    private void loadUsersList() {
        client.sendMessage("/allUsers");
        observableList = FXCollections.observableList(client.getObservableList());
    }


    private String getUserFromOpenedTab() {
        for (Map.Entry<String, MessagingTab> entry : openTabs.entrySet()) {
            if (entry.getValue().getTab().isSelected()) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String messageCommand(String receiver, String message) {
        return "/msg " + receiver + " " + message;
    }
}
