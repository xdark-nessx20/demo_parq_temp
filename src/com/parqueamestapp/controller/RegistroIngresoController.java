package com.parqueamestapp.controller;

import com.parqueamestapp.model.RegistroIngreso;
import com.parqueamestapp.model.Usuario;
import com.parqueamestapp.services.OperadorService;
import com.parqueamestapp.services.RegistroIngresoService;
import com.parqueamestapp.services.TipoVehiculoService;
import com.parqueamestapp.services.VehiculoService;
import com.parqueamestapp.services.exceptions.TicketAbiertoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/registros-ingreso")
public class RegistroIngresoController extends HttpServlet {
    private static final String VISTA_TICKET = "/WEB-INF/views/registro-ingreso/ticket.jsp";
    private static final String VISTA_LISTAR = "/WEB-INF/views/registro-ingreso/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/registro-ingreso/registrar.jsp";
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final RegistroIngresoService service = new RegistroIngresoService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private final OperadorService operadorService = new OperadorService();
    private final TipoVehiculoService tipoService = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.setAttribute("error", "Debe iniciar sesión");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        String placa = request.getParameter("placa");
        String tipoNombre = request.getParameter("tipoVehiculo");

        if (!vehiculoService.placaValidaPara(tipoNombre, placa)) {
            request.setAttribute("error", "La placa no corresponde al tipo (Carro: ABC-123 · Moto: ABC-12A)");
            mostrarFormulario(request, response);
            return;
        }

        // Si el vehiculo no existe, se crea SIN dueno: el cliente lo reclama luego
        // en la app (asi el operador no puede "asignarselo" a quien no es).
        var vehiculo = vehiculoService.findByPlaca(placa).orElse(null);
        if (vehiculo == null) {
            if (!vehiculoService.save(placa, null, tipoNombre)) {
                request.setAttribute("error", "No se pudo registrar el vehículo (revise la placa según el tipo)");
                mostrarFormulario(request, response);
                return;
            }
            vehiculo = vehiculoService.findByPlaca(placa).orElse(null);
        }
        if (vehiculo == null) {
            request.setAttribute("error", "No se pudo registrar el vehículo");
            mostrarFormulario(request, response);
            return;
        }

        try {
            var ticket = service.registrarIngreso(vehiculo.id(), LocalDateTime.now(), usuario.id());
            request.setAttribute("ticket", ticket.get());
            request.setAttribute("sinDueno", vehiculo.owner() == null);
            cargarMapas(request);
            request.getRequestDispatcher(VISTA_TICKET).forward(request, response);
        } catch (TicketAbiertoException e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
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
            case "estado" -> estado(request, response);
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    // Endpoint ligero para el auto-refresco "en vivo" del listado de registros.
    private void estado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var idVehiculo = parseUuid(request.getParameter("idVehiculo"));
        var lista = idVehiculo.isPresent() ? service.historialVehiculo(idVehiculo.get()) : service.listar();
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().print(firma(lista));
    }

    private String firma(java.util.List<RegistroIngreso> ingresos) {
        var sb = new StringBuilder();
        for (var r : ingresos) sb.append(r.id()).append(':').append(r.horaSalida()).append('|');
        return sb.toString();
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var ingresos = service.listar();
        request.setAttribute("ingresos", ingresos);
        request.setAttribute("firma", firma(ingresos));
        request.setAttribute("refrescoUrl", "/registros-ingreso?accion=estado");
        cargarMapas(request);
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
        request.setAttribute("firma", firma(historial));
        request.setAttribute("refrescoUrl", "/registros-ingreso?accion=estado&idVehiculo=" + idVehiculo.get());
        cargarMapas(request);
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
        request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
    }

    // Mapas id->texto para mostrar placas y nombres en vez de UUIDs.
    private void cargarMapas(HttpServletRequest request) {
        var placas = new HashMap<UUID, String>();
        for (var v : vehiculoService.findAll()) placas.put(v.id(), v.placa());
        var operadores = new HashMap<UUID, String>();
        for (var o : operadorService.findAll()) operadores.put(o.id(), o.nombre());
        request.setAttribute("placas", placas);
        request.setAttribute("operadores", operadores);
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
