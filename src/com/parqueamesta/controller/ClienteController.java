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
    private static final String VISTA_LISTAR = "/WEB-INF/views/cliente/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/cliente/registrar.jsp";
    private static final String VISTA_EDITAR = "/WEB-INF/views/cliente/editar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";

    private final ClienteService service = new ClienteService();

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
        request.setAttribute("clientes", service.findAll());
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var c = service.findByCedula(request.getParameter("cedula"));
        if (c.isEmpty()) {
            request.setAttribute("error", "No se encontro el cliente");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("cliente", c.get());
        request.getRequestDispatcher("/WEB-INF/views/cliente/details.jsp").forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");
        String contrasena = request.getParameter("contrasena");

        if (service.existeCedula(cedula)) {
            request.setAttribute("error", "Ya existe un usuario con la cédula " + cedula);
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            return;
        }

        if (service.save(nombre, cedula, contrasena)) {
            response.sendRedirect(request.getContextPath() + "/clientes");
        } else {
            request.setAttribute("error", "No se pudo registrar. El nombre debe tener minimo 6 letras, la cedula 8 a 10 digitos y la contrasena minimo 8 caracteres con 1 mayuscula y 1 simbolo");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        }
    }

    private void mostrarEditar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var c = service.findByCedula(request.getParameter("cedula"));
        if (c.isEmpty()) {
            request.setAttribute("error", "No se encontro el cliente");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("cliente", c.get());
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");

        if (service.update(cedula, nombre)) {
            response.sendRedirect(request.getContextPath() + "/clientes");
        } else {
            request.setAttribute("error", "No se pudo actualizar (revise el nombre)");
            request.setAttribute("cliente", service.findByCedula(cedula).orElse(null));
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (service.delete(request.getParameter("cedula"))) {
            request.getSession().setAttribute("mensaje", "Cliente eliminado");
        } else {
            request.getSession().setAttribute("error", "No se pudo eliminar (puede tener vehículos asociados)");
        }
        response.sendRedirect(request.getContextPath() + "/clientes");
    }
}
