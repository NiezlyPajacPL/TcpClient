package main.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.managers.Delay;
import main.managers.Logger;
import main.managers.settings.Settings;
import main.network.TcpClient;

import java.util.*;


public class ClientSceneController {

    //const
    private final String REFRESHING = "REFRESHING...";
    private final String ALL_USERS_COMMAND = "/allUsers";
    private final String LOGOUT = "/logout";
    private final String REFRESH = "Refresh";

    private TcpClient client;
    private ArrayList<String> onlineUsers;
    private Map<String, MessagingTab> openTabs = new HashMap<>();
    private String userName;
    private ArrayList<String> filteredUsers = new ArrayList<>();
    private boolean filterRunning = false;
    private Settings settings;

    public void construct(TcpClient client, String userName,Settings settings) {
        this.client = client;
        this.userName = userName;
        this.settings = settings;
        loadUsersList();
        if(settings.isSoundMuted()){
            muteSounds.setSelected(true);
        }
    }

    @FXML
    CheckMenuItem muteSounds;
    @FXML
    Button refreshButton;
    @FXML
    ListView<String> usersListView;
    @FXML
    public TabPane tabPane;
    @FXML
    TextArea messageTextArea;
    @FXML
    TextField searchField;

    //TABS
    public boolean isTabOpen(String sender) {
        return openTabs.get(sender) != null;
    }

    public void addNewTab(String user) {
        TextArea textArea = new TextArea();
        Tab userTab = TabCreator.createTab(user, textArea);

        userTab.setOnClosed(event -> openTabs.remove(user));
        tabPane.getTabs().add(userTab);
        openTabs.put(user, new MessagingTab(userTab, textArea));
    }

    public void applyMessageToTab(String sender, String message) {
        openTabs.get(sender).getTextArea().appendText(message + "\n");
    }

    //FXML
    @FXML
    protected void onRefresh() {
        if (!refreshButton.getText().equals(REFRESHING)) {
            client.sendMessage(ALL_USERS_COMMAND);
            usersListView.getItems().clear();
            loadUsersList();
        }
    }

    @FXML
    protected void handleMouseClick() {
        String user = usersListView.getSelectionModel().getSelectedItem();
        Logger.clickedOnUser(user);

        if (!user.isBlank() && !isTabOpen(user)) {
            addNewTab(user);
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

    @FXML
    protected void menuOnLogout() {
        client.sendMessage(LOGOUT);
        Logger.loggingOut();
        Delay.delay(1000, new Runnable() {
            @Override
            public void run() {
                Platform.exit();
            }
        });
    }

    @FXML
    protected void onMuteSounds(){
        if(muteSounds.isSelected()){
            Logger.mutedSounds();
            settings.muteSounds();
        }else {
            Logger.unMutedSounds();
            settings.unMuteSounds();
        }
    }

    //PRIVATE METHODS
    private void updateSearchFilter() {
        searchField.textProperty().addListener(observable -> {
            String filter = searchField.getText();
            ObservableList<String> usersView = usersListView.getItems();
            if (!filter.isBlank()) {
                usersView.clear();
                usersView.addAll(onlineUsers);
            } else {
                for (String onlineUser : onlineUsers) {
                    if (onlineUser.contains(filter)) {
                        filteredUsers.clear();
                        filteredUsers.add(onlineUser);
                        usersView.clear();
                        usersView.addAll(filteredUsers);
                    } else {
                        filteredUsers.clear();
                        usersView.clear();
                        usersView.addAll(filteredUsers);
                    }
                }
            }
        });
    }

    private void loadUsersList() {
        if (!filterRunning) {
            client.sendMessage(ALL_USERS_COMMAND);
            refreshButton.setText(REFRESHING);
            Delay.delay(2000, new Runnable() {
                @Override
                public void run() {
                    onlineUsers = client.getOnlineUsers();
                    onlineUsers.remove(userName);
                    updateSearchFilter();
                    filterRunning = true;
                    usersListView.getItems().addAll(onlineUsers);
                    refreshButton.setText(REFRESH);
                }
            });
        } else {
            refreshButton.setText(REFRESHING);
            Delay.delay(2000, new Runnable() {
                @Override
                public void run() {
                    onlineUsers = client.getOnlineUsers();
                    onlineUsers.remove(userName);
                    updateSearchFilter();
                    usersListView.getItems().addAll(filteredUsers);
                    refreshButton.setText(REFRESH);
                }
            });
        }
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
