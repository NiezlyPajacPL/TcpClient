package main.messageTypes;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;


public class MessageJsonDeserializer implements JsonDeserializer<MessageType> {

    //TYPES
    private final String REGISTER_TYPE = "REGISTER";
    private final String LOGIN_TYPE = "LOGIN";
    private final String LOGOUT_TYPE = "LOGOUT";
    private final String ONLINE_USERS_TYPE = "ONLINE_USERS";
    private final String MESSAGE_TYPE = "MESSAGE";
    //JSON ELEMENTS
    private final String USERS = "usersList";
    private final String SENDER = "sender";
    private final String IS_LOGIN_SUCCESSFUL = "isLoginSuccessful";
    private final String IS_REGISTRATION_SUCCESSFUL = "isRegistrationSuccessful";
    private final String TYPE = "type";

    @Override
    public MessageType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get(TYPE).getAsString();

        if (Objects.equals(type, REGISTER_TYPE)) {
            return new Register(jsonObject.get(IS_REGISTRATION_SUCCESSFUL).getAsBoolean());
        } else if (Objects.equals(type, LOGIN_TYPE)) {
            return new Login(jsonObject.get(IS_LOGIN_SUCCESSFUL).getAsBoolean());
        } else if (Objects.equals(type, ONLINE_USERS_TYPE)) {
            return new UsersListReceiver(jsonObject.get(USERS).getAsJsonArray());
        } else if (Objects.equals(type, LOGOUT_TYPE)) {
            return new Logout(jsonObject.get(MESSAGE_TYPE.toLowerCase(Locale.ROOT)).getAsString());
        } else if (Objects.equals(type, MESSAGE_TYPE)) {
            return new Message(jsonObject.get(SENDER).getAsString(), jsonObject.get(MESSAGE_TYPE.toLowerCase(Locale.ROOT)).getAsString());
        }
        return null;
    }
}
