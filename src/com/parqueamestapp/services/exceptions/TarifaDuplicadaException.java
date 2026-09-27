package com.parqueamestapp.services.exceptions;

public class TarifaDuplicadaException extends BaseException {
    public TarifaDuplicadaException() {
        super("Ya existe una tarifa para ese tipo de vehículo en ese año. Elimínala o actualízala en vez de crear otra.");
    }
}
