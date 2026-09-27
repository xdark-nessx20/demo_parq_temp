package com.parqueamestapp.services.exceptions;

public class TicketAbiertoException extends BaseException {
    public TicketAbiertoException() {
        super("El vehiculo ya tiene un ticket abierto");
    }
}