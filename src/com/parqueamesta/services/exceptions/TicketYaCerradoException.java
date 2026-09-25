package com.parqueamesta.services.exceptions;

public class TicketYaCerradoException extends BaseException {
    public TicketYaCerradoException() {
        super("El ticket ya fue cerrado");
    }
}