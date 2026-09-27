package com.parqueamesta.controller;

import com.parqueamesta.services.TipoVehiculoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/tipos-vehiculo")
public class TipoVehiculoController extends HttpServlet {
    private final TipoVehiculoService service = new TipoVehiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        boolean wasSaved = service.save(nombre, descripcion);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/tipos-vehiculo");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
            request.getRequestDispatcher("/WEB-INF/views/tipo-vehiculo/registrar.jsp").forward(request, response);
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
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida: " + accion);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var tipos = service.findAll();
        request.setAttribute("tipos", tipos);
        request.getRequestDispatcher("/WEB-INF/views/tipo-vehiculo/listar.jsp").forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var tipo = service.findByName(request.getParameter("nombre"));

        if (tipo.isEmpty()) {
            request.setAttribute("error", "Tipo de Vehiculo no encontrado");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("tipo", tipo.get());
        request.getRequestDispatcher("/WEB-INF/views/tipo-vehiculo/details.jsp").forward(request, response);
    }

}
