package com.parqueamestapp.controller;

import com.parqueamestapp.model.Pago;
import com.parqueamestapp.model.Usuario;
import com.parqueamestapp.services.PagoService;
import com.parqueamestapp.services.RegistroIngresoService;
import com.parqueamestapp.services.TarifaService;
import com.parqueamestapp.services.VehiculoService;
import com.parqueamestapp.services.exceptions.TarifaNoEncontradaException;
import com.parqueamestapp.services.exceptions.TicketNoEncontradoException;
import com.parqueamestapp.services.exceptions.TicketYaCerradoException;
import com.parqueamestapp.services.exceptions.VehiculoNoEncontradoException;
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

@WebServlet("/pagos")
public class PagoController extends HttpServlet {
    private static final String VISTA_PAGAR = "/WEB-INF/views/pago/pagar.jsp";
    private static final String VISTA_LISTAR = "/WEB-INF/views/pago/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/pago/registrar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final RegistroIngresoService registroService = new RegistroIngresoService();
    private final PagoService pagoService = new PagoService();
    private final TarifaService tarifaService = new TarifaService();
    private final VehiculoService vehiculoService = new VehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.setAttribute("error", "Debe iniciar sesión");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        // Solo el Operador efectua pagos (el Gerente solo los ve).
        if (usuario.rol() != com.parqueamestapp.model.Rol.OPERADOR) {
            request.getSession().setAttribute("error", "Solo el operador puede registrar salidas y cobrar");
            response.sendRedirect(request.getContextPath() + "/pagos");
            return;
        }

        // El operador cobra un pago pendiente (registra el pago en el sistema).
        if ("cobrar".equals(request.getParameter("accion"))) {
            var idPago = parseUuid(request.getParameter("idPago"));
            if (idPago.isPresent() && pagoService.marcarPagado(idPago.get())) {
                request.getSession().setAttribute("mensaje", "Pago cobrado correctamente");
            } else {
                request.getSession().setAttribute("error", "No se pudo cobrar el pago");
            }
            response.sendRedirect(request.getContextPath() + "/pagos");
            return;
        }

        var idRegistro = parseUuid(request.getParameter("idRegistro"));
        if (idRegistro.isEmpty()) {
            request.setAttribute("error", "Seleccione un ticket");
            mostrarFormulario(request, response);
            return;
        }

        try {
            // El operador de la salida es el usuario de la sesión.
            var pago = registroService.registrarSalida(idRegistro.get(), LocalDateTime.now(), usuario.id());
            request.setAttribute("pago", pago.get());
            cargarDesglose(request, idRegistro.get());
            request.getRequestDispatcher(VISTA_PAGAR).forward(request, response);
        } catch (TarifaNoEncontradaException | VehiculoNoEncontradoException e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
        } catch (TicketYaCerradoException e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
        } catch (TicketNoEncontradoException e) {
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

    // Endpoint ligero para el auto-refresco "en vivo" del listado de pagos.
    private void estado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().print(firma(pagoService.listar()));
    }

    private String firma(java.util.List<Pago> pagos) {
        var sb = new StringBuilder();
        for (var p : pagos) sb.append(p.id()).append(':').append(p.pagado()).append('|');
        return sb.toString();
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var pagos = pagoService.listar();
        request.setAttribute("pagos", pagos);
        request.setAttribute("firma", firma(pagos));
        request.setAttribute("refrescoUrl", "/pagos?accion=estado");
        cargarPlacas(request);
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
        cargarDesglose(request, idRegistro.get());
        request.getRequestDispatcher(VISTA_PAGAR).forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarFormulario(request, response);
    }

    // Carga los tickets abiertos (para el desplegable) y muestra el formulario.
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tickets", registroService.ticketsActivos());
        cargarPlacas(request);
        request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
    }

    // Carga el desglose del pago (vehiculo, horas, tarifa por hora) para la pantalla de pago.
    private void cargarDesglose(HttpServletRequest request, UUID idRegistro) {
        var registro = registroService.buscar(idRegistro).orElse(null);
        if (registro == null) return;

        request.setAttribute("horaEntrada", registro.getHoraEntradaTexto());
        request.setAttribute("horaSalida", registro.getHoraSalidaTexto());

        var vehiculo = vehiculoService.findById(registro.idVehiculo()).orElse(null);
        if (vehiculo == null) return;

        request.setAttribute("vehiculoPlaca", vehiculo.placa());
        if (vehiculo.tipo() == null) return;

        tarifaService.tarifaActual(vehiculo.tipo().id()).ifPresent(t -> {
            request.setAttribute("valorHoraTexto", com.parqueamestapp.util.Formato.moneda(t.valorHora()));
            request.setAttribute("horas",
                    tarifaService.horasCobradasPublico(registro.horaEntrada(), registro.horaSalida()));
        });
    }

    // Mapa registro->placa para mostrar el vehiculo en el listado de pagos.
    private void cargarPlacas(HttpServletRequest request) {
        var placasPorRegistro = new HashMap<UUID, String>();
        for (var r : registroService.listar()) {
            var placa = vehiculoService.findById(r.idVehiculo()).map(v -> v.placa()).orElse("");
            placasPorRegistro.put(r.id(), placa);
        }
        request.setAttribute("placasPorRegistro", placasPorRegistro);
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
