package com.parqueamesta.controller;

import com.parqueamesta.services.PagoService;
import com.parqueamesta.services.RegistroIngresoService;
import com.parqueamesta.services.exceptions.TarifaNoEncontradaException;
import com.parqueamesta.services.exceptions.TicketNoEncontradoException;
import com.parqueamesta.services.exceptions.TicketYaCerradoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/pagos")
public class PagoController extends HttpServlet {
    private static final String VISTA_PAGAR = "/WEB-INF/views/pago/pagar.jsp";
    private static final String VISTA_LISTAR = "/WEB-INF/views/pago/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/pago/registrar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";

    private final RegistroIngresoService registroService = new RegistroIngresoService();
    private final PagoService pagoService = new PagoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var idRegistro = parseUuid(request.getParameter("idRegistro"));
        var idOperador = parseUuid(request.getParameter("idOperador"));
        var idTipoVehiculo = parseUuid(request.getParameter("idTipoVehiculo"));

        if (idRegistro.isEmpty() || idOperador.isEmpty() || idTipoVehiculo.isEmpty()) {
            request.setAttribute("error", "Los IDs no son válidos");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            return;
        }

        try {
            var pago = registroService.registrarSalida(
                    idRegistro.get(),
                    LocalDateTime.now(),
                    idOperador.get(),
                    idTipoVehiculo.get());

            request.setAttribute("pago", pago.get());
            request.getRequestDispatcher(VISTA_PAGAR).forward(request, response);
        } catch (TarifaNoEncontradaException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        } catch (TicketYaCerradoException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        } catch (TicketNoEncontradoException e) {
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
        var pagos = pagoService.listar();
        request.setAttribute("pagos", pagos);
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var idRegistro = parseUuid(request.getParameter("idRegistro"));

        if (idRegistro.isEmpty()) {
            request.setAttribute("error", "El ID del registro no es válido");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }

        var pago = pagoService.buscarPorRegistro(idRegistro.get());

        if (pago.isEmpty()) {
            request.setAttribute("error", "Pago no encontrado");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("pago", pago.get());
        request.getRequestDispatcher(VISTA_PAGAR).forward(request, response);
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