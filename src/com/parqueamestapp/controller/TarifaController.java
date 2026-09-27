package com.parqueamestapp.controller;

import com.parqueamestapp.services.TarifaService;
import com.parqueamestapp.services.TipoVehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/tarifas")
public class TarifaController extends HttpServlet {
    private static final String VISTA_LISTAR = "/WEB-INF/views/tarifa/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/tarifa/registrar.jsp";

    private final TarifaService service = new TarifaService();
    private final TipoVehiculoService tipoService = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        if ("registrar".equals(accion)) {
            crear(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminar(request, response);
        } else {
            actualizarPrecio(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        accion = accion == null ? "listar" : accion;

        switch (accion) {
            case "listar" -> listar(request, response);
            case "registrar" -> registrar(request, response);
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var tarifas = service.listar();
        request.setAttribute("tarifas", tarifas);
        var tipoNombres = new HashMap<UUID, String>();
        for (var t : tipoService.findAll()) tipoNombres.put(t.id(), t.nombre());
        request.setAttribute("tipoNombres", tipoNombres);
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarFormulario(request, response);
    }

    // Carga los tipos de vehiculo (para el desplegable) y muestra el formulario.
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tipos", tipoService.findAll());
        request.setAttribute("anioActual", Year.now().getValue());
        request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
    }

    private void crear(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var idTipoVehiculo = parseUuid(request.getParameter("idTipoVehiculo"));
        var valorHora = parseBigDecimal(request.getParameter("valorHora"));
        var anioVigencia = parseInt(request.getParameter("anioVigencia"));

        if (idTipoVehiculo.isEmpty() || valorHora.isEmpty() || anioVigencia.isEmpty()) {
            request.setAttribute("error", "Los datos no son válidos");
            mostrarFormulario(request, response);
            return;
        }

        boolean ok;
        try {
            ok = service.save(idTipoVehiculo.get(), valorHora.get(), anioVigencia.get());
        } catch (com.parqueamestapp.services.exceptions.TarifaDuplicadaException e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
            return;
        }

        if (ok) {
            response.sendRedirect(request.getContextPath() + "/tarifas");
        } else {
            request.setAttribute("error", "No se pudo crear la tarifa (el valor debe ser mayor a 0)");
            mostrarFormulario(request, response);
        }
    }

    private void actualizarPrecio(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var idTarifa = parseUuid(request.getParameter("idTarifa"));
        var nuevoValor = parseBigDecimal(request.getParameter("nuevoValor"));

        if (idTarifa.isEmpty() || nuevoValor.isEmpty()) {
            request.setAttribute("error", "Los datos no son válidos");
            request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
            return;
        }

        boolean ok = service.actualizarPrecio(idTarifa.get(), nuevoValor.get());

        if (ok) {
            response.sendRedirect(request.getContextPath() + "/tarifas");
        } else {
            request.setAttribute("error", "No se pudo actualizar el precio (valor debe ser mayor a 0)");
            request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var idTarifa = parseUuid(request.getParameter("idTarifa"));

        if (idTarifa.isPresent() && service.delete(idTarifa.get())) {
            request.getSession().setAttribute("mensaje", "Tarifa eliminada");
        } else {
            request.getSession().setAttribute("error", "No se pudo eliminar la tarifa");
        }
        response.sendRedirect(request.getContextPath() + "/tarifas");
    }

    private Optional<UUID> parseUuid(String valor) {
        if (valor == null || valor.isBlank()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(valor));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> parseBigDecimal(String valor) {
        if (valor == null || valor.isBlank()) return Optional.empty();
        try {
            return Optional.of(new BigDecimal(valor));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseInt(String valor) {
        if (valor == null || valor.isBlank()) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(valor));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}