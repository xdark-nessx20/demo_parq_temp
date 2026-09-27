package com.parqueamesta.services.exceptions;

public class VehiculoNoEncontradoException extends BaseException {
    public VehiculoNoEncontradoException() {
        super("El vehículo del ticket no existe");
    }
}