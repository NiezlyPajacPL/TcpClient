package main.managers;

public final class Logger {

    //CLIENT SCENE
    public static void mutedSounds(){println("Log: Muted sounds");}

    public static void unMutedSounds(){println("Log: Un-muted sounds");}

    public static void loggingOut(){println("Log: Client is logging out..");}

    public static void clickedOnUser(String user){println("Log: Clicked on " + user);}

    //LOGIN SCENE

    public static void loginAttempt(){println("Log: Attempting to log in");}

    public static void loggedIn(){println("Log: Client logged in, closing login thread.");}

    //PRIVATE
    private static void println(String text){System.out.println(text);}

    private static void print(String text){System.out.print(text);}
}
