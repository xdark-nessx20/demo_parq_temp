package com.parqueamesta.services.exceptions;

public class TicketNoEncontradoException extends BaseException {
    public TicketNoEncontradoException() {
        super("El ticket no existe");
    }
}