package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.network.Client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class LoginController{

    public String login;
    public String password;
    public Map<String,String> loginAndPassword = new HashMap<>();

    @FXML
    PasswordField passwordField;
    @FXML
    TextField loginField;
    @FXML
    protected void onButtonClick() {
        login = loginField.getText();
        password = passwordField.getText();
        loginAndPassword.put(login,password);
    }

    public String getCommand() {
        return "/login " + login + " " + password;
    }

}
