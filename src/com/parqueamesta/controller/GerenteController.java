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
    private static final String VISTA_LISTAR = "/WEB-INF/views/gerente/listar.jsp";
    private static final String VISTA_REGISTRAR = "/WEB-INF/views/gerente/registrar.jsp";
    private static final String VISTA_EDITAR = "/WEB-INF/views/gerente/editar.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/views/error.jsp";

    private final GerenteService service = new GerenteService();

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
        request.setAttribute("gerentes", service.findAll());
        request.getRequestDispatcher(VISTA_LISTAR).forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var g = service.findByCedula(request.getParameter("cedula"));
        if (g.isEmpty()) {
            request.setAttribute("error", "No se encontro el gerente");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("gerente", g.get());
        request.getRequestDispatcher("/WEB-INF/views/gerente/details.jsp").forward(request, response);
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");
        String contrasena = request.getParameter("contrasena");

        if (!com.parqueamesta.util.PasswordPolicy.valida(contrasena)) {
            request.setAttribute("error", "La contraseña no cumple con los requisitos necesarios");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            return;
        }

        if (service.existeCedula(cedula)) {
            request.setAttribute("error", "Ya existe un usuario con la cédula " + cedula);
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
            return;
        }

        if (service.save(nombre, cedula, contrasena)) {
            response.sendRedirect(request.getContextPath() + "/gerentes");
        } else {
            request.setAttribute("error", "No se pudo registrar. Revisa el nombre (mínimo 6 letras) y la cédula (8 a 10 dígitos)");
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        }
    }

    private void mostrarEditar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var g = service.findByCedula(request.getParameter("cedula"));
        if (g.isEmpty()) {
            request.setAttribute("error", "No se encontro el gerente");
            request.getRequestDispatcher(VISTA_ERROR).forward(request, response);
            return;
        }
        request.setAttribute("gerente", g.get());
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");

        if (service.update(cedula, nombre)) {
            response.sendRedirect(request.getContextPath() + "/gerentes");
        } else {
            request.setAttribute("error", "No se pudo actualizar (revise el nombre)");
            request.setAttribute("gerente", service.findByCedula(cedula).orElse(null));
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (service.delete(request.getParameter("cedula"))) {
            request.getSession().setAttribute("mensaje", "Gerente eliminado");
        } else {
            request.getSession().setAttribute("error", "No se pudo eliminar");
        }
        response.sendRedirect(request.getContextPath() + "/gerentes");
    }
}
