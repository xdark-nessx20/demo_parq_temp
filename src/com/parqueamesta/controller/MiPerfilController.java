package com.parqueamesta.controller;

import com.parqueamesta.model.Usuario;
import com.parqueamesta.services.PerfilService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Perfil del usuario logueado (cualquier rol): editar su nombre y cambiar su contrasena.
@WebServlet("/mi-perfil")
public class MiPerfilController extends HttpServlet {
    private static final String VISTA = "/WEB-INF/views/mi-perfil.jsp";
    private static final String VISTA_LOGIN = "/WEB-INF/views/login.jsp";

    private final PerfilService service = new PerfilService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (usuarioDeSesion(request) == null) {
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var usuario = usuarioDeSesion(request);
        if (usuario == null) {
            request.getRequestDispatcher(VISTA_LOGIN).forward(request, response);
            return;
        }

        if ("contrasena".equals(request.getParameter("accion"))) {
            var actual = request.getParameter("actual");
            var nueva = request.getParameter("nueva");
            if (service.cambiarContrasena(usuario.cedula(), actual, nueva)) {
                request.getSession().setAttribute("mensaje", "Contraseña actualizada");
            } else {
                request.getSession().setAttribute("error",
                        "No se pudo cambiar la contraseña (revise la actual y los requisitos de la nueva)");
            }
        } else {
            var actualizado = service.actualizarNombre(usuario.cedula(), request.getParameter("nombre"));
            if (actualizado.isPresent()) {
                request.getSession().setAttribute("usuario", actualizado.get());
                request.getSession().setAttribute("mensaje", "Datos actualizados");
            } else {
                request.getSession().setAttribute("error", "El nombre debe tener al menos 6 letras");
            }
        }
        response.sendRedirect(request.getContextPath() + "/mi-perfil");
    }

    private Usuario usuarioDeSesion(HttpServletRequest request) {
        var session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuario");
    }
}
