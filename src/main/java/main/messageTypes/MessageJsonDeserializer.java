package main.messageTypes;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;


public class MessageJsonDeserializer implements JsonDeserializer<MessageType> {

    private final String REGISTER = "REGISTER";
    private final String LOGIN = "LOGIN";
    private final String LOGOUT = "LOGOUT";
    private final String ONLINE_USERS = "ONLINE_USERS";
    private final String MESSAGE = "MESSAGE";
    private final String USERS = "usersList";
    private final String SENDER = "sender";
    private final String IS_LOGIN_SUCCESSFUL = "isLoginSuccessful";
    private final String IS_REGISTRATION_SUCCESSFUL = "isRegistrationSuccessful";

    @Override
    public MessageType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if (Objects.equals(type, REGISTER)) {
            return new Register(jsonObject.get(IS_REGISTRATION_SUCCESSFUL).getAsBoolean());
        } else if (Objects.equals(type, LOGIN)) {
            return new Login(jsonObject.get(IS_LOGIN_SUCCESSFUL).getAsBoolean());
        } else if (Objects.equals(type, ONLINE_USERS)) {
            return new UsersListReceiver(jsonObject.get(USERS).getAsJsonArray());
        } else if (Objects.equals(type, LOGOUT)) {
            return new Logout(jsonObject.get(MESSAGE.toLowerCase(Locale.ROOT)).getAsString());
        } else if (Objects.equals(type, MESSAGE)) {
        return new Message(jsonObject.get(SENDER).getAsString(),jsonObject.get(MESSAGE.toLowerCase(Locale.ROOT)).getAsString());
        }
        return null;
    }
}
