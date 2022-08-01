package main.managers;

public final class SubtitlesPrinter {

    public static void printEnter(int enterCount){
        for(int i = 0; i < enterCount; i++){
            println("");
        }
    }
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

    public static void printErrorMessageTooLarge(){print("Your message is too long.");}

    public static void printReceivedMessage(String message){println(message);}

    public static void printLostConnection(){println("Lost connection with server. Trying to reconnect.");}

    public static void printConnectionProblems(){println("There are problems with connecting to the server. Trying again.");}

    public static void printReconnectingUnsuccessful(){println("Reconnecting was not successful. Trying again in 5 seconds");}

    public static void printConnectionEstablished(){println("Connection successfully established.");}

    public static void printErrorSomethingWentWrong(){println("Something went wrong.");}

    public static void printBye(){println("GoodBye! See you soon!");}

    private static void println(String text){System.out.println(text);}

    private static void print(String text){System.out.print(text);}
}
