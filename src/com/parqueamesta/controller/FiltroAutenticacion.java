package com.parqueamesta.controller;

import com.parqueamesta.model.Rol;
import com.parqueamesta.model.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Exige sesion para todo (excepto login/index/css) y restringe ciertas secciones al Gerente.
@WebFilter("/*")
public class FiltroAutenticacion implements Filter {

    // Secciones que solo puede ver el Gerente.
    private static final List<String> SOLO_GERENTE =
            List.of("/tarifas", "/operadores", "/gerentes", "/tipos-vehiculo");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        var path = request.getRequestURI().substring(request.getContextPath().length());

        if (esPublica(path)) {
            chain.doFilter(req, res);
            return;
        }

        var session = request.getSession(false);
        var usuario = session == null ? null : (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (usuario.rol() == Rol.CLIENTE) {
            // El cliente solo ve su cuenta (sus datos y contrasena viven dentro de Mi cuenta).
            if (!path.startsWith("/mi-cuenta")) {
                response.sendRedirect(request.getContextPath() + "/mi-cuenta");
                return;
            }
        } else {
            // Operador/Gerente no entran al portal del cliente.
            if (path.startsWith("/mi-cuenta")) {
                session.setAttribute("error", "No tiene permiso para esa sección");
                response.sendRedirect(request.getContextPath() + "/dentro");
                return;
            }
            // Secciones exclusivas del Gerente.
            if (esSoloGerente(path) && usuario.rol() != Rol.GERENTE) {
                session.setAttribute("error", "No tiene permiso para esa sección");
                response.sendRedirect(request.getContextPath() + "/dentro");
                return;
            }
            // Escrituras de operacion (ingreso, salida, cobro): solo el Operador.
            // El Gerente SI puede ver el listado de ingresos, pero no registrar.
            if (esEscrituraOperacion(request, path) && usuario.rol() != Rol.OPERADOR) {
                session.setAttribute("error", "Solo el operador puede registrar ingresos y salidas");
                response.sendRedirect(request.getContextPath() + "/dentro");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private boolean esPublica(String path) {
        if (path.isEmpty() || path.equals("/")) return true;
        return path.equals("/index.jsp")
                || path.equals("/login")
                || path.equals("/registro")
                || path.equals("/logout")
                || path.startsWith("/css/");
    }

    private boolean esSoloGerente(String path) {
        return SOLO_GERENTE.stream().anyMatch(path::startsWith);
    }

    // Escrituras de operacion: POST en ingreso/pago/dar-salida, o el formulario de registro.
    private boolean esEscrituraOperacion(HttpServletRequest request, String path) {
        if (!path.equals("/dentro") && !path.equals("/registros-ingreso") && !path.equals("/pagos")) {
            return false;
        }
        if ("POST".equalsIgnoreCase(request.getMethod())) return true;
        return "registrar".equals(request.getParameter("accion"));
    }
}
