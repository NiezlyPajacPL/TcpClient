package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController{

    public String login;
    public String password;

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
