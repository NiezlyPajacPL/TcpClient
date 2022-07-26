package main.helpers;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MessagingTab {

   private Tab tab;
   private TextArea textArea;

    public MessagingTab(Tab tab, TextArea textArea){
        this.tab = tab;
        this.textArea = textArea;
    }

    public Tab getTab() {
        return tab;
    }

    public TextArea getTextArea() {
        return textArea;
    }

}
