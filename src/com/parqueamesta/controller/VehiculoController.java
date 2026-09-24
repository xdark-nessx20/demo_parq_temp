package com.parqueamesta.controller;

import com.parqueamesta.services.VehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class VehiculoController extends HttpServlet {
    private final VehiculoService service = new VehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String ownerCedula = request.getParameter("cedulaCliente");
        String tipoNombre = request.getParameter("tipoVehiculo");

        boolean wasSaved = service.save(placa, marca, ownerCedula, tipoNombre);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
            request.getRequestDispatcher("WEB-INF/view/vehiculos/registrar.jsp").forward(request, response);
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
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        boolean wasDeleted = service.delete(request.getParameter("placa"));
        //No sé qué hacer ahora
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var vehiculo = service.findByPlaca(request.getParameter("placa"));

        if (vehiculo.isEmpty()){
            request.setAttribute("error", "No se encontro el vehiculo");
            request.getRequestDispatcher("/WEB-INF/vistas/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("vehiculo", vehiculo);
        request.getRequestDispatcher("/WEB-INF/view/vehiculo/details.jsp").forward(request, response);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var vehiculos = service.findAll();
        request.setAttribute("vehiculos", vehiculos);
        request.getRequestDispatcher("/WEB-INF/views/vehiculo/listar.jsp").forward(request, response);
    }
}
