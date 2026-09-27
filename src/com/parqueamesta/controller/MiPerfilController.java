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
            var resultado = service.cambiarContrasena(usuario.cedula(),
                    request.getParameter("actual"), request.getParameter("nueva"));

            // Peticion AJAX (fetch): se devuelve el codigo y el JSP abre el modal sin recargar.
            if ("fetch".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().print(resultado.name());
                return;
            }

            request.getSession().setAttribute(resultado == PerfilService.ResultadoCambio.OK ? "mensaje" : "error",
                    mensajeContrasena(resultado));
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

    private String mensajeContrasena(PerfilService.ResultadoCambio resultado) {
        return switch (resultado) {
            case OK -> "Contraseña actualizada correctamente.";
            case ACTUAL_INCORRECTA -> "La contraseña actual no coincide con la del sistema.";
            case IGUAL_A_ANTERIOR -> "La nueva contraseña es igual a la anterior.";
            case NO_CUMPLE -> "La nueva contraseña no cumple con los requisitos.";
        };
    }

    private Usuario usuarioDeSesion(HttpServletRequest request) {
        var session = request.getSession(false);
        return session == null ? null : (Usuario) session.getAttribute("usuario");
    }
}
