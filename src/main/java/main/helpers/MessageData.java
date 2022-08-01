package main.helpers;

import main.messageTypes.MessageType;

public class MessageData{
    private final String sender;
    private final String message;

    public MessageData(String sender, String message){
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
