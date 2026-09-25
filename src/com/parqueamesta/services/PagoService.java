package com.parqueamesta.services;

import com.parqueamesta.model.Pago;
import com.parqueamesta.persistence.RepositorioPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PagoService {
    private final RepositorioPago repo = new RepositorioPago();

    public PagoService() {
    }

    public boolean guardar(UUID idRegistroIngreso, BigDecimal valor, LocalDateTime fechaPago) {
        if (idRegistroIngreso == null || valor == null || valor.signum() <= 0 || fechaPago == null) return false;
        return repo.save(new Pago(idRegistroIngreso, valor, fechaPago));
    }

    public Optional<Pago> buscarPorRegistro(UUID idRegistroIngreso) {
        if (idRegistroIngreso == null) return Optional.empty();
        return repo.getByRegistroIngreso(idRegistroIngreso);
    }

    public List<Pago> listar() {
        return repo.getAll();
    }
}