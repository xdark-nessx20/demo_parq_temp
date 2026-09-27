package com.parqueamesta.controller;

import com.parqueamesta.model.MovimientoCliente;
import com.parqueamesta.model.Usuario;
import com.parqueamesta.model.Vehiculo;
import com.parqueamesta.services.PagoService;
import com.parqueamesta.services.RegistroIngresoService;
import com.parqueamesta.services.TarifaService;
import com.parqueamesta.services.TipoVehiculoService;
import com.parqueamesta.services.VehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Portal del cliente: registra sus vehiculos, ve sus tickets y paga online.
@WebServlet("/mi-cuenta")
public class MiCuentaController extends HttpServlet {
    private static final String VISTA = "/WEB-INF/views/mi-cuenta.jsp";
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final VehiculoService vehiculoService = new VehiculoService();
    private final TipoVehiculoService tipoService = new TipoVehiculoService();
    private final RegistroIngresoService registroService = new RegistroIngresoService();
    private final PagoService pagoService = new PagoService();
    private final TarifaService tarifaService = new TarifaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.setAttribute("error", "Debe iniciar sesión");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        // Endpoint ligero para el auto-refresco "en vivo": devuelve una firma del estado.
        if ("estado".equals(request.getParameter("accion"))) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().print(firma(listarMovimientos(usuario)));
            return;
        }

        var movimientos = listarMovimientos(usuario);
        request.setAttribute("movimientos", movimientos);
        request.setAttribute("firma", firma(movimientos));
        request.setAttribute("tipos", tipoService.findAll());
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    // Firma del estado actual (cambia si cambia algun movimiento).
    private String firma(List<MovimientoCliente> movimientos) {
        var sb = new StringBuilder();
        for (var m : movimientos) {
            sb.append(m.getPlaca()).append(':').append(m.getEstado()).append(':').append(m.getValor()).append('|');
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.setAttribute("error", "Debe iniciar sesión");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        String accion = request.getParameter("accion");

        if ("registrarVehiculo".equals(accion)) {
            var placa = request.getParameter("placa");
            var tipoNombre = request.getParameter("tipoVehiculo");
            if (vehiculoService.existePlaca(placa)) {
                request.getSession().setAttribute("error", "Ya existe un vehículo con la placa " + placa);
            } else if (vehiculoService.save(placa, usuario.cedula(), tipoNombre)) {
                request.getSession().setAttribute("mensaje", "Vehículo registrado correctamente");
            } else {
                request.getSession().setAttribute("error", "No se pudo registrar (revise la placa según el tipo)");
            }
        } else {
            var idPago = parseUuid(request.getParameter("idPago"));
            if (idPago.isPresent() && pagoService.marcarPagado(idPago.get())) {
                request.getSession().setAttribute("mensaje", "Pago realizado correctamente");
            } else {
                request.getSession().setAttribute("error", "No se pudo procesar el pago");
            }
        }

        response.sendRedirect(request.getContextPath() + "/mi-cuenta");
    }

    // Fusion: un renglon por vehiculo del cliente, con el estado segun su ultimo registro.
    private List<MovimientoCliente> listarMovimientos(Usuario cliente) {
        var lista = new ArrayList<MovimientoCliente>();

        for (var v : vehiculoService.findByOwner(cliente.id())) {
            var tipo = v.tipo() != null ? v.tipo().nombre() : "-";
            var registros = registroService.historialVehiculo(v.id()); // ordenado por entrada DESC

            if (registros.isEmpty()) {
                lista.add(new MovimientoCliente(v.placa(), tipo, "-", "-", "-", "Sin movimientos", null));
                continue;
            }

            var r = registros.get(0); // el mas reciente
            var pagoOpt = pagoService.buscarPorRegistro(r.id());

            if (r.horaSalida() == null) {
                var valor = valorEstimado(v, r.horaEntrada());
                lista.add(new MovimientoCliente(v.placa(), tipo, r.getHoraEntradaTexto(), "-",
                        "$" + com.parqueamesta.util.Formato.moneda(valor), "En parqueadero", null));
            } else if (pagoOpt.isPresent()) {
                var p = pagoOpt.get();
                lista.add(new MovimientoCliente(v.placa(), tipo, r.getHoraEntradaTexto(), r.getHoraSalidaTexto(),
                        "$" + com.parqueamesta.util.Formato.moneda(p.valor()), p.getEstado(), p.id()));
            } else {
                lista.add(new MovimientoCliente(v.placa(), tipo, r.getHoraEntradaTexto(), r.getHoraSalidaTexto(),
                        "-", "Cerrado", null));
            }
        }
        return lista;
    }

    private BigDecimal valorEstimado(Vehiculo v, LocalDateTime entrada) {
        if (v.tipo() == null) return BigDecimal.ZERO;
        return tarifaService.tarifaActual(v.tipo().id())
                .map(t -> tarifaService.calcular(t.valorHora(), entrada, LocalDateTime.now()))
                .orElse(BigDecimal.ZERO);
    }

    private Usuario usuarioDeSesion(HttpServletRequest request) {
        var session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuario");
    }

    private Optional<UUID> parseUuid(String valor) {
        if (valor == null || valor.isBlank()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(valor));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
