package main.controllers;

import javafx.scene.control.Label;

public interface LoggingInController {

    String getLogin();

    void setLoginToNull();

    String getLoginCommand();

    Label getSomethingWentWrongLabel();

}
