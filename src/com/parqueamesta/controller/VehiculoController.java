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
    private final VehiculoService service = new VehiculoService();
    private final ClienteService clienteService = new ClienteService();
    private final TipoVehiculoService tipoService = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String placa = request.getParameter("placa");
        String ownerCedula = request.getParameter("cedulaCliente");
        String tipoNombre = request.getParameter("tipoVehiculo");

        boolean wasSaved = service.save(placa, ownerCedula, tipoNombre);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
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
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        mostrarFormulario(request, response);
    }

    // Carga clientes y tipos de vehiculo (para los desplegables) y muestra el formulario.
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setAttribute("clientes", clienteService.findAll());
        request.setAttribute("tipos", tipoService.findAll());
        request.getRequestDispatcher("/WEB-INF/views/vehiculo/registrar.jsp").forward(request, response);
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

    private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var vehiculo = service.findByPlaca(request.getParameter("placa"));

        if (vehiculo.isEmpty()){
            request.setAttribute("error", "No se encontro el vehiculo");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("vehiculo", vehiculo.get());
        request.getRequestDispatcher("/WEB-INF/views/vehiculo/details.jsp").forward(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var vehiculos = service.findAll();
        request.setAttribute("vehiculos", vehiculos);
        request.getRequestDispatcher("/WEB-INF/views/vehiculo/listar.jsp").forward(request, response);
    }
}
