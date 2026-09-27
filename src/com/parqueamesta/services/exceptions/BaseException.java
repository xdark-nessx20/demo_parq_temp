package com.parqueamesta.services.exceptions;

public class BaseException extends RuntimeException {
    public BaseException(String mensaje) {
        super(mensaje);
    }
}