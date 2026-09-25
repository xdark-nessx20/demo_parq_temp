package com.parqueamesta.controller;

import com.parqueamesta.services.ClienteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/clientes")
public class ClienteController extends HttpServlet {
    private final ClienteService service = new ClienteService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");

        boolean wasSaved = service.save(nombre, cedula);

        if (wasSaved) {
            response.sendRedirect(request.getContextPath() + "/clientes");
        } else {
            request.setAttribute("error", "No se ha podido realizar la operacion");
            request.getRequestDispatcher("/WEB-INF/views/cliente/registrar.jsp").forward(request, response);
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

    private void listar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var clientes = service.findAll();
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("/WEB-INF/views/cliente/listar.jsp").forward(request, response);
    }

    private  void buscar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var c = service.findByCedula(request.getParameter("cedula"));

        if (c.isEmpty()){
            request.setAttribute("error", "No se encontro el cliente");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("cliente", c.get());
        request.getRequestDispatcher("/WEB-INF/views/cliente/details.jsp").forward(request, response);
    }
}
