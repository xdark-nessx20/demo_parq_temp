package com.parqueamestapp.services.exceptions;

public class VehiculoSinDuenoException extends BaseException {
    public VehiculoSinDuenoException() {
        super("No se puede dar salida: el vehículo no tiene dueño. El cliente debe reclamarlo primero en la app.");
    }
}
