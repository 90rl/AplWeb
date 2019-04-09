package p.lodzka.service;

public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException(String message){
        super(message);
    }
}
