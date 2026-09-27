package com.parqueamestapp.services.exceptions;

public class VehiculoNoEncontradoException extends BaseException {
    public VehiculoNoEncontradoException() {
        super("El vehículo del ticket no existe");
    }
}