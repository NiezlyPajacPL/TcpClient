package main.managers;

import com.google.gson.*;
import main.messageTypes.*;


public class JsonMapperImpl implements JsonMapper {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(MessageType.class, new MessageJsonDeserializer())
            .setPrettyPrinting()
            .create();

    @Override
    public MessageType mapJson(String json) {
        System.out.println(json);
        MessageType messageType = gson.fromJson(json, MessageType.class);
        return gson.fromJson(json, MessageType.class);
    }
}