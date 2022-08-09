package main.managers.console;

public final class ConsolePrinter {

    //CLIENT SCENE
    public static void mutedSounds(){println("Log: Muted sounds");}

    public static void unMutedSounds(){println("Log: Un-muted sounds");}

    public static void loggingOut(){println("Log: Client is logging out..");}

    public static void clickedOnUser(String user){println("Log: Clicked on " + user);}

    //LOGIN SCENE

    public static void loginAttempt(){println("Log: Attempting to log in");}

    public static void loggedIn(){println("Log: Client logged in, closing login thread.");}


    //OTHERS
    public static void printErrorMessageTooLarge(){print("Your message is too long.");}

    public static void printReceivedMessage(String message){println(message);}

    public static void printLostConnection(){println("Lost connection with server. Trying to reconnect.");}

    public static void printConnectionProblems(){println("There are problems with connecting to the server. Trying again.");}

    public static void printReconnectingUnsuccessful(){println("Reconnecting was not successful. Trying again in 5 seconds");}

    public static void printConnectionEstablished(){println("Connection successfully established.");}

    public static void printErrorSomethingWentWrong(){println("Something went wrong.");}

    public static void printBye(){println("GoodBye! See you soon!");}

    public static void printHelper(){
        println("////////////POMOC////////////");
        println("Lista wszystkich uzytkownikow: /allUsers");
        println("Wysylanie wiadomosci do danego uzytownika: /msg <nick uzytkownika> <twoja wiadomosc>");
        println("Wysylanie wiadomosci do poprzedniego uzytkownika: /r <twoja wiadomosc>");
        println("Wylogowanie: /logout");
        println("Klikniecie enter bez wpisywania zadnej komendy powtorzy ostatnia komende.");
    }

    public static void printIsHelpNeeded(){println("Jesli potrzebujesz pomocy wpisz '/help'");}

    public static void printRegistrationRequest(){
        println("Type '/register <YourNickname> <Password>");
        println("or if you already have existing account Type '/Login <YourNickname> <Password> ");
    }

    private static void println(String text){System.out.println(text);}

    private static void print(String text){System.out.print(text);}
}
