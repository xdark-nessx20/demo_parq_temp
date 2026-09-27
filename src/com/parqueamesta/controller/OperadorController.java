package com.parqueamesta.controller;

import com.parqueamesta.services.OperadorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/operadores")
public class OperadorController extends HttpServlet {
    private final OperadorService service = new OperadorService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");

        boolean wasSaved = service.save(nombre, cedula);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/operadores");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
            request.getRequestDispatcher("/WEB-INF/views/operador/registrar.jsp").forward(request, response);
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
        var operadores = service.findAll();
        request.setAttribute("operadores", operadores);
        request.getRequestDispatcher("/WEB-INF/views/operador/listar.jsp").forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var o = service.findByCedula(request.getParameter("cedula"));

        if (o.isEmpty()) {
            request.setAttribute("error", "No se encontro el operador");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("operador", o.get());
        request.getRequestDispatcher("/WEB-INF/views/operador/details.jsp").forward(request, response);
    }
}
