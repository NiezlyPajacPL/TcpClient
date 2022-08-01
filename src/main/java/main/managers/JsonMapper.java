package main.managers;

import main.messageTypes.MessageType;

public interface JsonMapper {

    MessageType mapJson(String json);
}
