package com.parqueamesta.controller;

import com.parqueamesta.services.ClienteService;
import com.parqueamesta.services.TipoVehiculoService;
import com.parqueamesta.services.VehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vehiculos")
public class VehiculoController extends HttpServlet {
    private static final String VISTA_LISTAR = "/WEB-INF/views/vehiculo/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/vehiculo/registrar.jsp";
    private static final String VISTA_EDITAR = "/WEB-INF/views/vehiculo/editar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";

    private final VehiculoService service = new VehiculoService();
    private final ClienteService clienteService = new ClienteService();
    private final TipoVehiculoService tipoService = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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
            throws IOException, ServletException {
        String accion = request.getParameter("accion");
        accion = accion == null ? "listar" : accion;

        switch (accion) {
            case "listar" -> listar(request, response);
            case "buscar" -> buscar(request, response);
            case "registrar" -> mostrarFormulario(request, response);
            case "editar" -> mostrarEditar(request, response);
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        boolean wasDeleted = service.delete(request.getParameter("placa"));
        if (wasDeleted) {
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el vehículo");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("vehiculos", service.findAll());
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var vehiculo = service.findByPlaca(request.getParameter("placa"));
        if (vehiculo.isEmpty()) {
            request.setAttribute("error", "No se encontro el vehiculo");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("vehiculo", vehiculo.get());
        request.getRequestDispatcher("/WEB-INF/views/vehiculo/details.jsp").forward(request, response);
    }

    // Carga clientes y tipos (desplegables) y muestra el formulario.
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setAttribute("clientes", clienteService.findAll());
        request.setAttribute("tipos", tipoService.findAll());
        request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String placa = request.getParameter("placa");
        String ownerCedula = request.getParameter("cedulaCliente");
        String tipoNombre = request.getParameter("tipoVehiculo");

        if (service.save(placa, ownerCedula, tipoNombre)) {
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion (revise la placa según el tipo)");
            mostrarFormulario(request, response);
        }
    }

    private void mostrarEditar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var vehiculo = service.findByPlaca(request.getParameter("placa"));
        if (vehiculo.isEmpty()) {
            request.setAttribute("error", "No se encontro el vehiculo");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("vehiculo", vehiculo.get());
        request.setAttribute("tipos", tipoService.findAll());
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String placa = request.getParameter("placa");
        String tipoNombre = request.getParameter("tipoVehiculo");

        if (service.updateTipo(placa, tipoNombre)) {
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } else {
            request.setAttribute("error", "No se pudo actualizar el vehículo");
            request.setAttribute("vehiculo", service.findByPlaca(placa).orElse(null));
            request.setAttribute("tipos", tipoService.findAll());
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        service.delete(request.getParameter("placa"));
        response.sendRedirect(request.getContextPath() + "/vehiculos");
    }
}
