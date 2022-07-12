package managers;

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
        println("or if you already have existing account Type '/login <YourNickname> <Password> ");
    }

    public static void printErrorClientNotLogged(){println("You need to log in first!");}

    public static void printErrorMessageTooLarge(){print("Your message is too long.");}

    public static void printErrorWrongCommand(){println("You've typed the command wrongly.");}

    public static void printErrorClientIsLogged(){println("You are already logged in.");}

    public static void printErrorSomethingWentWrong(){println("Something went wrong.");}

    public static void printBye(){println("GoodBye! See you soon!");}

    private static void println(String text){System.out.println(text);}

    private static void print(String text){System.out.print(text);}

}
