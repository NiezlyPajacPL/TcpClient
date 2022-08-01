package main.messageTypes;

public class JsonMessageObj {
    public Types type;
    public String message;

    public JsonMessageObj(Types type, String message){
        this.type = type;
        this.message = message;
    }
}
