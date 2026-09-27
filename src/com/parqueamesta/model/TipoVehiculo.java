package com.parqueamesta.model;

import java.util.Objects;
import java.util.UUID;

public class TipoVehiculo {
    private UUID id;
    private String nombre;
    private String formatoPlaca;

    public TipoVehiculo(UUID id, String nombre, String formatoPlaca) {
        this.id = id;
        this.nombre = nombre;
        this.formatoPlaca = formatoPlaca;
    }

    public TipoVehiculo(String nombre, String formatoPlaca) {
        this.nombre = nombre;
        this.formatoPlaca = formatoPlaca;
    }

    //Getters con record style
    public UUID id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    public String formatoPlaca() {
        return formatoPlaca;
    }

    // Getters JavaBean (para JSP/EL)
    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFormatoPlaca() {
        return formatoPlaca;
    }

    // Texto legible del formato, para mostrar en pantalla.
    public String getFormatoTexto() {
        return esMoto() ? "Moto (AAA-00A)" : "Carro (AAA-000)";
    }

    public boolean esMoto() {
        return "MOTO".equalsIgnoreCase(formatoPlaca);
    }

    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFormatoPlaca(String formatoPlaca) {
        this.formatoPlaca = formatoPlaca;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TipoVehiculo that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "TipoVehiculo {id: %s, nombre: %s, formatoPlaca: %s}"
                .formatted(id != null ? id.toString() : "", nombre, formatoPlaca);
    }
}
