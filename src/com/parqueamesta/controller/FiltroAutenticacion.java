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

    // Secciones de operacion: solo el Operador (el Gerente administra, no opera).
    private static final List<String> SOLO_OPERADOR =
            List.of("/registros-ingreso", "/pagos");

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
            // El cliente solo puede ver su propia cuenta.
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
            // Secciones de operacion: el Gerente no opera.
            if (esSoloOperador(path) && usuario.rol() != Rol.OPERADOR) {
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

    private boolean esSoloOperador(String path) {
        return SOLO_OPERADOR.stream().anyMatch(path::startsWith);
    }
}
