package com.parqueamesta.controller;

import com.parqueamesta.model.Rol;
import com.parqueamesta.persistence.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final UsuarioRepository repo = new UsuarioRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String cedula = request.getParameter("cedula");
        String contrasena = request.getParameter("contrasena");

        var usuario = repo.autenticar(cedula, contrasena);

        if (usuario.isEmpty()) {
            request.setAttribute("error", "Cédula o contraseña incorrectas");
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        request.getSession().setAttribute("usuario", usuario.get());
        response.sendRedirect(request.getContextPath() + rutaPorRol(usuario.get().rol()));
    }

    // Cada rol aterriza en su pantalla principal.
    private String rutaPorRol(Rol rol) {
        return switch (rol) {
            case GERENTE -> "/tarifas";
            case OPERADOR -> "/registros-ingreso";
            case CLIENTE -> "/vehiculos";
        };
    }
}
