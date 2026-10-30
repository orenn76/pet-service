package com.ninyo.pet.exception;

public class PetException extends RuntimeException {

    public PetException() {
    }

    public PetException(String message) {
        super(message);
    }

    public PetException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetException(Throwable cause) {
        super(cause);
    }
}