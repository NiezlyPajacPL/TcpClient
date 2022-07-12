package managers;

import java.nio.charset.StandardCharsets;

public class MessageHelper {
    private final String REGISTER = "/register";
    private final String LOGIN = "/login";
    private final String MESSAGE = "/msg";
    private final String ALL_USERS = "/allUsers";

    SubtitlesPrinter subtitlesPrinter;

    public MessageHelper(SubtitlesPrinter subtitlesPrinter){
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public boolean messageCanBeSent(String input, boolean isClientLoggedIn) {
        if (commandCanBeSent(input) && !disableLoginAttempt(input, isClientLoggedIn) && !messageIsTooLarge(input)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean commandCanBeSent(String input) {
        if (input.contains(REGISTER) || input.contains(LOGIN) || input.contains(MESSAGE)) {
            if (checkIfInputLengthMatchesExpected(3, input)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
    private boolean messageIsTooLarge(String input) {
        byte[] array = input.getBytes(StandardCharsets.UTF_8);
        if (input.length() >= 200000) {
            subtitlesPrinter.printErrorMessageTooLarge();
            return true;
        }
        return false;
    }
    private boolean disableLoginAttempt(String message, boolean isClientLoggedIn) {
        if ((isClientLoggedIn && !message.contains(REGISTER) && !message.contains(LOGIN))) {
            return false;
        } else if (!isClientLoggedIn && message.contains(REGISTER) || !isClientLoggedIn && message.contains(LOGIN)) {
            return false;
        }
        subtitlesPrinter.printErrorSomethingWentWrong();
        return true;
    }


    /*
    private boolean forceRegistration(String message, boolean isClientLoggedIn){
         if ((!isClientLoggedIn && !message.contains(REGISTER)) && (!isClientLoggedIn && !message.contains(LOGIN))) {
            return true;
    }
         return false;
    }

    private boolean disableLoginAttempt(String message, boolean isClientLoggedIn) {
        if ((isClientLoggedIn && message.contains(REGISTER) || isClientLoggedIn && message.contains(LOGIN))) {
            subtitlesPrinter.printErrorClientIsLogged();
            return true;
        }
        return false;
    }
*/

    private boolean checkIfInputLengthMatchesExpected(int expectedNumberOfWords, String input) {
        String[] words = input.split("\\s+");

        if (words.length >= expectedNumberOfWords) {
            return true;
        } else {
            return false;
        }
    }

}
