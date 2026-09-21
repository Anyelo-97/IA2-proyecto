package com.example.demo.curso.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, String identificador) {
        super(String.format("%s con identificador '%s' no fue encontrado.", recurso, identificador));
    }
}
