package main.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.helpers.MessagingTab;
import main.helpers.TabCreator;
import main.managers.Logger;
import main.managers.SoundHandler;
import main.managers.settings.Settings;
import main.messageTypes.Message;
import main.network.MessageListener;
import main.network.TcpClient;

import java.util.*;

public class ClientSceneController {

    //const
    private final String ALL_USERS_COMMAND = "/allUsers";
    private final String LOGOUT = "/logout";

    private TcpClient client;
    private Settings settings;
    private String userName;

    private ArrayList<String> onlineUsers;
    private final Map<String, MessagingTab> openTabs = new HashMap<>();
    private String currentFilter;
    private boolean isRefreshingInProgress = false;


    public void construct(TcpClient client, String userName, Settings settings) {
        this.client = client;
        this.userName = userName;
        this.settings = settings;

        setFilterListener();
        client.sendMessage(ALL_USERS_COMMAND);
        if (settings.isSoundMuted()) {
            muteSoundsCheckBox.setSelected(true);
        }
        client.setMessageListener(new MessageListener() {
            @Override
            public void onMessageReceived(Message messageData) {
                handleIncomingMessage(messageData);
            }

            @Override
            public void onUsersListReceived() {
                addOnlineUsersToListView();
            }

            @Override
            public void onLogout() {
                Logger.loggingOut();
                Platform.exit();
            }
        });
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
            isRefreshingInProgress = false;
        }
    }

    @FXML
    protected void handleMouseClick() {
        String user = usersListView.getSelectionModel().getSelectedItem();
        Logger.clickedOnUser(user);
        if (newTabCanBeOpened(user)) {
            addNewTab(user);
        }
    }

    @FXML
    protected void onSendButtonClicked() {
        String receiver = getUserFromOpenedTab();
        String message = messageTextArea.getText();
        if (messageCanBeSent(receiver, message)) {
            client.sendMessage(messageCommand(receiver, message));
            applyMessageToTab(receiver,(userName + ": " + message));
            messageTextArea.clear();
        }
    }

    @FXML
    protected void menuOnLogout() {
        client.sendMessage(LOGOUT);
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

    //FILTER
    private void setFilterListener() {
        searchField.textProperty().addListener(observable -> {
            currentFilter = searchField.getText();
            onFilterChanged();
        });
    }

    private void onFilterChanged() {
        ObservableList<String> usersView = usersListView.getItems();
        if (currentFilter.isBlank()) {
            Platform.runLater(() -> {
                usersView.clear();
                usersView.addAll(onlineUsers);
            });
        } else {
            ArrayList<String> filteredUsers = new ArrayList<>();
            for (String onlineUser : onlineUsers) {
                if (onlineUser.contains(currentFilter)) {
                    filteredUsers.add(onlineUser);
                }
                Platform.runLater(() -> {
                    usersView.clear();
                    usersView.addAll(filteredUsers);
                });
            }
        }
    }

    private void addOnlineUsersToListView() {
        onlineUsers = client.getOnlineUsers();
        onlineUsers.remove(userName);
        usersListView.getItems().addAll(onlineUsers);
    }

    //TABS
    private void addNewTab(String user) {
        TextArea textArea = new TextArea();
        Tab userTab = TabCreator.createTab(user, textArea);

        userTab.setOnClosed(event -> openTabs.remove(user));
        tabPane.getTabs().add(userTab);
        openTabs.put(user, new MessagingTab(userTab, textArea));
    }

    private void handleIncomingMessage(Message messageData) {
        String sender = messageData.getSender();
        if (newTabCanBeOpened(sender)) {
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

    private void applyMessageToTab(String sender, String message) {
        openTabs.get(sender).getTextArea().appendText(message + "\n");
    }

    private String getUserFromOpenedTab() {
        for (Map.Entry<String, MessagingTab> entry : openTabs.entrySet()) {
            if (entry.getValue().getTab().isSelected()) {
                return entry.getKey();
            }
        }
        return null;
    }
    private boolean newTabCanBeOpened(String user) {
        return user != null && !user.isBlank() && openTabs.get(user) == null;
    }

    //
    private String messageCommand(String receiver, String message) {
        return "/msg " + receiver + " " + message;
    }

    private boolean messageCanBeSent(String receiver, String message) {
        return receiver != null && !receiver.isBlank() && !message.isBlank() && message.length() < 120;
    }
}
