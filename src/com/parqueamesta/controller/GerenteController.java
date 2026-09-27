package com.parqueamesta.controller;

import com.parqueamesta.services.GerenteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/gerentes")
public class GerenteController extends HttpServlet {
    private final GerenteService service = new GerenteService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");

        boolean wasSaved = service.save(nombre, cedula);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/gerentes");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
            request.getRequestDispatcher("/WEB-INF/views/gerente/registrar.jsp").forward(request, response);
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

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var gerentes = service.findAll();
        request.setAttribute("gerentes", gerentes);
        request.getRequestDispatcher("/WEB-INF/views/gerente/listar.jsp").forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var g = service.findByCedula(request.getParameter("cedula"));

        if (g.isEmpty()) {
            request.setAttribute("error", "No se encontro el gerente");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("gerente", g.get());
        request.getRequestDispatcher("/WEB-INF/views/gerente/details.jsp").forward(request, response);
    }
}
