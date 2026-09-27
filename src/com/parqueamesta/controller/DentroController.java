package com.parqueamesta.controller;

import com.parqueamesta.model.Usuario;
import com.parqueamesta.model.VehiculoDentro;
import com.parqueamesta.services.RegistroIngresoService;
import com.parqueamesta.services.VehiculoService;
import com.parqueamesta.services.exceptions.BaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Panel principal: muestra los vehiculos que estan dentro y permite darles salida rapido.
@WebServlet("/dentro")
public class DentroController extends HttpServlet {
    private static final String VISTA = "/WEB-INF/views/dentro.jsp";
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final RegistroIngresoService registroService = new RegistroIngresoService();
    private final VehiculoService vehiculoService = new VehiculoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.setAttribute("error", "Debe iniciar sesión");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        // Endpoint ligero para el auto-refresco "en vivo".
        if ("estado".equals(request.getParameter("accion"))) {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().print(firma(listarDentro()));
            return;
        }

        var lista = listarDentro();
        request.setAttribute("vehiculosDentro", lista);
        request.setAttribute("firma", firma(lista));
        request.setAttribute("refrescoUrl", "/dentro?accion=estado");
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    // Firma del estado (cambia si cambia el conjunto de vehiculos adentro).
    private String firma(List<VehiculoDentro> lista) {
        var sb = new StringBuilder();
        for (var v : lista) {
            sb.append(v.getId()).append('|');
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

        var idRegistro = parseUuid(request.getParameter("idRegistro"));
        if (idRegistro.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dentro");
            return;
        }

        try {
            var pago = registroService.registrarSalida(idRegistro.get(), LocalDateTime.now(), usuario.id());
            pago.ifPresent(p -> request.getSession().setAttribute("mensaje",
                    "Salida registrada. Total a pagar: $" + p.valor()));
        } catch (BaseException e) {
            request.getSession().setAttribute("error", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/dentro");
    }

    private List<VehiculoDentro> listarDentro() {
        var lista = new ArrayList<VehiculoDentro>();
        for (var r : registroService.ticketsActivos()) {
            var v = vehiculoService.findById(r.idVehiculo()).orElse(null);
            var placa = v != null ? v.placa() : "(desconocido)";
            var tipo = (v != null && v.tipo() != null) ? v.tipo().nombre() : "-";
            lista.add(new VehiculoDentro(r.id(), placa, tipo, r.getHoraEntradaTexto(), tiempoDentro(r.horaEntrada())));
        }
        return lista;
    }

    private String tiempoDentro(LocalDateTime entrada) {
        var d = Duration.between(entrada, LocalDateTime.now());
        long h = d.toHours();
        long m = d.toMinutesPart();
        return h > 0 ? h + "h " + m + "m" : m + "m";
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
