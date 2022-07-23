package main.scenes.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ClientSceneController {
    public String login;
    public String password;

    @FXML
    public Label connectionProblems;
    @FXML
    public Label wrongPassword;
    @FXML
    Button loginButton;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField loginField;
    @FXML
    protected void onButtonClick() {
        login = loginField.getText();
        password = passwordField.getText();
    }

    public String getCommand() {
        return "/login " + login + " " + password;
    }
}
