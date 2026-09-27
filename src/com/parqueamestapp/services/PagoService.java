package com.parqueamestapp.services;

import com.parqueamestapp.model.Pago;
import com.parqueamestapp.persistence.RepositorioPago;
import com.parqueamestapp.services.exceptions.PagoYaProcesadoException;

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

    // Marca un pago como pagado (lo paga el cliente online o lo cobra el operador).
    // Regla: no se puede procesar un pago que ya fue pagado.
    public boolean marcarPagado(UUID idPago) {
        if (idPago == null) return false;
        var pago = repo.get(idPago).orElseThrow(PagoYaProcesadoException::new);
        if (pago.pagado()) {
            throw new PagoYaProcesadoException();
        }
        return repo.marcarPagado(idPago);
    }
}