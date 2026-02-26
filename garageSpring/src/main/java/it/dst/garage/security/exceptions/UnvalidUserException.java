package it.dst.garage.security.exceptions;

public class UnvalidUserException extends RuntimeException {
    public UnvalidUserException(String message) {
        super(message);
    }
    
}
