package com.parqueamestapp.controller;

import com.parqueamestapp.services.TipoVehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/tipos-vehiculo")
public class TipoVehiculoController extends HttpServlet {
    private static final String VISTA_LISTAR = "/WEB-INF/views/tipo-vehiculo/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/tipo-vehiculo/registrar.jsp";
    private static final String VISTA_EDITAR = "/WEB-INF/views/tipo-vehiculo/editar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";

    private final TipoVehiculoService service = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("editar".equals(accion)) {
            editar(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminar(request, response);
        } else {
            registrar(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        accion = accion == null ? "listar" : accion;

        switch (accion) {
            case "listar" -> listar(request, response);
            case "buscar" -> buscar(request, response);
            case "registrar" -> request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            case "editar" -> mostrarEditar(request, response);
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setAttribute("tipos", service.findAll());
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var tipo = service.findByName(request.getParameter("nombre"));
        if (tipo.isEmpty()) {
            request.setAttribute("error", "Tipo de Vehiculo no encontrado");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("tipo", tipo.get());
        request.getRequestDispatcher("/WEB-INF/views/tipo-vehiculo/details.jsp").forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        String formatoPlaca = request.getParameter("formatoPlaca");

        if (service.save(nombre, formatoPlaca)) {
            response.sendRedirect(request.getContextPath() + "/tipos-vehiculo");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion (nombre mínimo 3 letras y formato de placa obligatorio)");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        }
    }

    private void mostrarEditar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var id = parseUuid(request.getParameter("id"));
        var tipo = id.isPresent() ? service.findById(id.get()) : Optional.<com.parqueamestapp.model.TipoVehiculo>empty();

        if (tipo.isEmpty()) {
            request.setAttribute("error", "Tipo de Vehiculo no encontrado");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("tipo", tipo.get());
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var id = parseUuid(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String formatoPlaca = request.getParameter("formatoPlaca");

        if (id.isPresent() && service.update(id.get(), nombre, formatoPlaca)) {
            response.sendRedirect(request.getContextPath() + "/tipos-vehiculo");
        } else {
            request.setAttribute("error", "No se pudo actualizar (revise el nombre y el formato de placa)");
            request.setAttribute("tipo", id.isPresent() ? service.findById(id.get()).orElse(null) : null);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var id = parseUuid(request.getParameter("id"));
        if (id.isPresent() && service.delete(id.get())) {
            request.getSession().setAttribute("mensaje", "Tipo eliminado");
        } else {
            request.getSession().setAttribute("error", "No se pudo eliminar (puede tener tarifas o vehículos asociados)");
        }
        response.sendRedirect(request.getContextPath() + "/tipos-vehiculo");
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
