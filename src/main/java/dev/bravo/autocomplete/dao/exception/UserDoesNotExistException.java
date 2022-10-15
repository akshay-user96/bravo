package dev.bravo.autocomplete.dao.exception;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String msg) {
        super(msg);
    }
}
