package com.parqueamestapp.services.exceptions;

public class BaseException extends RuntimeException {
    public BaseException(String mensaje) {
        super(mensaje);
    }
}