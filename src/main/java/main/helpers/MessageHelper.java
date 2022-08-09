package main.helpers;

import main.managers.SubtitlesPrinter;

import java.nio.charset.StandardCharsets;

public class MessageHelper {
    private final String REGISTER_COMMAND = "/register";
    private final String LOGIN_COMMAND = "/login";
    private final String MESSAGE_COMMAND = "/msg";

    private SubtitlesPrinter subtitlesPrinter;

    public MessageHelper(SubtitlesPrinter subtitlesPrinter){
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public boolean messageCanBeSent(String input, boolean isClientLoggedIn) {
        return commandCanBeSent(input) && !disableLoginAttempt(input, isClientLoggedIn) && !messageIsTooLarge(input);
    }

    private boolean commandCanBeSent(String input) {
        if (input.contains(REGISTER_COMMAND) || input.contains(LOGIN_COMMAND) || input.contains(MESSAGE_COMMAND)) {
            return checkIfInputLengthMatchesExpected(3, input);
        }
        return true;
    }
    private boolean messageIsTooLarge(String input) {
        byte[] array = input.getBytes(StandardCharsets.UTF_8);
        if (input.length() >= 200000) {
            SubtitlesPrinter.printErrorMessageTooLarge();
            return true;
        }
        return false;
    }
    private boolean disableLoginAttempt(String message, boolean isClientLoggedIn) {
        if ((isClientLoggedIn && !message.contains(REGISTER_COMMAND) && !message.contains(LOGIN_COMMAND))) {
            return false;
        } else if (!isClientLoggedIn && message.contains(REGISTER_COMMAND) || !isClientLoggedIn && message.contains(LOGIN_COMMAND)) {
            return false;
        }
        SubtitlesPrinter.printErrorSomethingWentWrong();
        return true;
    }

    private boolean checkIfInputLengthMatchesExpected(int expectedNumberOfWords, String input) {
        String[] words = input.split("\\s+");

        return words.length >= expectedNumberOfWords;
    }

}
