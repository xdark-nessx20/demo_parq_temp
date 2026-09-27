package com.parqueamesta.controller;

import com.parqueamesta.services.RegistroIngresoService;
import com.parqueamesta.services.exceptions.TicketAbiertoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/registros-ingreso")
public class RegistroIngresoController extends HttpServlet {
    private static final String VISTA_TICKET = "/WEB-INF/views/registro-ingreso/ticket.jsp";
    private static final String VISTA_LISTAR = "/WEB-INF/views/registro-ingreso/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/registro-ingreso/registrar.jsp";

    private final RegistroIngresoService service = new RegistroIngresoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var idVehiculo = parseUuid(request.getParameter("idVehiculo"));
        var idOperador = parseUuid(request.getParameter("idOperador"));

        if (idVehiculo.isEmpty() || idOperador.isEmpty()) {
            request.setAttribute("error", "Los IDs no son válidos");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            return;
        }

        try {
            var ticket = service.registrarIngreso(
                    idVehiculo.get(),
                    LocalDateTime.now(),
                    idOperador.get());

            request.setAttribute("ticket", ticket.get());
            request.getRequestDispatcher(VISTA_TICKET).forward(request, response);
        } catch (TicketAbiertoException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        accion = accion == null ? "listar" : accion;

        switch (accion) {
            case "listar" -> listar(request, response);
            case "buscar" -> buscar(request, response);
            case "registrar" -> registrar(request, response);
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var ingresos = service.listar();
        request.setAttribute("ingresos", ingresos);
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var idVehiculo = parseUuid(request.getParameter("idVehiculo"));

        if (idVehiculo.isEmpty()) {
            request.setAttribute("error", "El ID del vehículo no es válido");
            request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
            return;
        }

        var historial = service.historialVehiculo(idVehiculo.get());
        request.setAttribute("ingresos", historial);
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
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