package main.helpers;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class TabCreator {

    public static Tab createTab(String userName, TextArea textArea){
        Tab userTab = new Tab();
        userTab.setText(userName);
        textArea.setEditable(false);
        userTab.setContent(textArea);
        return userTab;
    }

}
