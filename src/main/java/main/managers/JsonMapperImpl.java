package main.managers;

import com.google.gson.*;
import main.messageTypes.*;

public class JsonMapperImpl implements JsonMapper {

    private Gson gson = new Gson();
    private Message message;
    private Login login;
    private Register register;
    private Logout logout;
    private UsersListReceiver usersListReceiver;

    @Override
    public MessageType mapJson(String json) {
        if (isLogin(json)) {
            System.out.println("Client logged In");
            return login;
        } else if (isMessage(json)) {
            System.out.println("Received message"); //todo logs
            return message;
        } else if (isUsersList(json)) {
            System.out.println("User received users list");
            return usersListReceiver;
        } else if (isRegister(json)) {
            System.out.println("Client registered");
            return register;
        } else if (isLogout(json)) {
            System.out.println("Client logged out.");
            return logout;
        }
        return null;
    }

    private boolean isMessage(String json) {
        try {
            message = gson.fromJson(json, Message.class);
        } catch (Exception e) {
            return false;

        }
        return message.type == Types.MESSAGE;
    }

    private boolean isLogin(String json) {
        try {
            login = gson.fromJson(json, Login.class);
        } catch (Exception e) {
            return false;

        }
        return login.type == Types.LOGIN;
    }

    private boolean isUsersList(String json) {
        try {
            usersListReceiver = gson.fromJson(json, UsersListReceiver.class);
        } catch (Exception e) {
            return false;
        }
        return usersListReceiver.type == Types.ONLINE_USERS;
    }

    private boolean isRegister(String json) {
        try {
            register = gson.fromJson(json, Register.class);
        } catch (Exception e) {
            return false;
        }
        return register.type == Types.REGISTER;
    }

    private boolean isLogout(String json) {
        try {
            logout = gson.fromJson(json, Logout.class);
        } catch (Exception e) {
            return false;
        }
        return logout.type == Types.LOGOUT;
    }
}