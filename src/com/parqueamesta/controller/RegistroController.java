package com.parqueamesta.controller;

import com.parqueamesta.services.ClienteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Registro propio del cliente (crea una cuenta de rol CLIENTE).
@WebServlet("/registro")
public class RegistroController extends HttpServlet {
    private static final String VISTA = "/WEB-INF/views/registro.jsp";

    private final ClienteService clienteService = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("cedula");
        String contrasena = request.getParameter("contrasena");

        if (clienteService.existeCedula(cedula)) {
            request.setAttribute("error", "Ya existe una cuenta con la cédula " + cedula);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        if (clienteService.save(nombre, cedula, contrasena)) {
            request.getSession().setAttribute("mensaje", "Cuenta creada. Ya puedes iniciar sesión.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error",
                    "No se pudo crear la cuenta (nombre ≥ 6, cédula válida, contraseña ≥ 6)");
            request.getRequestDispatcher(VISTA).forward(request, response);
        }
    }
}
