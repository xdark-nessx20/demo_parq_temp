package com.parqueamesta.services.exceptions;

public class TarifaNoEncontradaException extends BaseException {
    public TarifaNoEncontradaException() {
        super("No hay tarifa vigente para este tipo de vehiculo");
    }
}