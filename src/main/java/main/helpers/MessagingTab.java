package main.helpers;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class MessagingTab {

   private final Tab tab;
   private final TextArea textArea;

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
