package main.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.managers.Delay;
import main.managers.Logger;
import main.managers.SoundHandler;
import main.managers.settings.Settings;
import main.messageTypes.Message;
import main.network.MessageListener;
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

    private boolean isRefreshingInProgress = false;
    private String currentFilter;

    public void construct(TcpClient client, String userName, Settings settings) {
        this.client = client;
        this.userName = userName;
        this.settings = settings;
        loadUsersList();
        if (settings.isSoundMuted()) {
            muteSoundsCheckBox.setSelected(true);
        }
        client.setMessageListener(messageData -> handleIncomingMessage(messageData));
    }

    @FXML
    CheckMenuItem muteSoundsCheckBox;
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

    //FXML
    @FXML
    protected void onRefresh() {
        if (!isRefreshingInProgress) {
            isRefreshingInProgress = true;
            client.sendMessage(ALL_USERS_COMMAND);
            usersListView.getItems().clear();
            loadUsersList();
            isRefreshingInProgress = false;
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
        if (messageCanBeSent(receiver, message)) {
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
    protected void onMuteSounds() {
        if (muteSoundsCheckBox.isSelected()) {
            Logger.mutedSounds();
            settings.muteSounds();
        } else {
            Logger.unMutedSounds();
            settings.unMuteSounds();
        }
    }

    //PRIVATE METHODS
    private void setFilterListener() {
        searchField.textProperty().addListener(observable -> {
            currentFilter = searchField.getText();
        });
    }

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
        ObservableList<String> usersView = usersListView.getItems();
        if (!filterRunning) {
            client.sendMessage(ALL_USERS_COMMAND);
            refreshButton.setText(REFRESHING);
            Delay.delay(2000, new Runnable() {
                @Override
                public void run() {
                    onlineUsers = client.getOnlineUsers();
                    onlineUsers.remove(userName);
                    updateSearchFilter();
                    setFilterListener();
                    filterRunning = true;
                    usersView.addAll(onlineUsers);
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
                    usersView.addAll(onlineUsers);
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

    private boolean messageCanBeSent(String receiver, String message) {
        return receiver != null && !receiver.isBlank() && !message.isBlank();
    }

    private void handleIncomingMessage(Message messageData) {
        String sender = messageData.getSender();
        if (!isTabOpen(sender)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addNewTab(sender);
                    applyMessageToTab(sender, messageData.getMessage());
                    if (!settings.isSoundMuted()) {
                        SoundHandler.playSound(SoundHandler.MESSAGE_INBOUND);
                    }
                }
            });
        } else {
            if (!settings.isSoundMuted()) {
                SoundHandler.playSound(SoundHandler.MESSAGE_IN_OPENED_TAB);
            }
            applyMessageToTab(sender, messageData.getMessage());
        }
    }

    //TABS
    private boolean isTabOpen(String sender) {
        return openTabs.get(sender) != null;
    }

    private void addNewTab(String user) {
        TextArea textArea = new TextArea();
        Tab userTab = TabCreator.createTab(user, textArea);

        userTab.setOnClosed(event -> openTabs.remove(user));
        tabPane.getTabs().add(userTab);
        openTabs.put(user, new MessagingTab(userTab, textArea));
    }

    private void applyMessageToTab(String sender, String message) {
        openTabs.get(sender).getTextArea().appendText(message + "\n");
    }


/*    private ArrayList<String> getUsersByFilter() {
        ArrayList<String> filteredUsers = new ArrayList<>();
        searchField.textProperty().addListener(observable -> {
            currentFilter = searchField.getText();
            if (currentFilter == null || currentFilter.isBlank()) {
                filteredUsers.addAll(onlineUsers);
            } else {
                for (String onlineUser : onlineUsers) {
                    if (onlineUser.contains(currentFilter)) {
                        filteredUsers.clear();
                        filteredUsers.add(onlineUser);
                *//*    usersView.clear();
                    usersView.addAll(filteredUsers);*//*
                    } else {
                        filteredUsers.clear();
   *//*                 usersView.clear();
                    usersView.addAll(filteredUsers);*//*
                    }
                }
            }
            return onlineUsers;
        });
        return onlineUsers;
    }*/
}
