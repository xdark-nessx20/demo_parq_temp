package com.parqueamestapp.services.exceptions;

public class PagoYaProcesadoException extends BaseException {
    public PagoYaProcesadoException() {
        super("Ese pago ya fue procesado.");
    }
}
