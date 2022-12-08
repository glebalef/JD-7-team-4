package pro.sky.telegrambot.exception;

public class WrongPhoneNumberException extends Exception{
    public WrongPhoneNumberException () {
        super();
    }
    public WrongPhoneNumberException (String message) {
        super(message);
    }
}
