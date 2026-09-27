package com.parqueamestapp.services.exceptions;

public class TicketNoEncontradoException extends BaseException {
    public TicketNoEncontradoException() {
        super("El ticket no existe");
    }
}