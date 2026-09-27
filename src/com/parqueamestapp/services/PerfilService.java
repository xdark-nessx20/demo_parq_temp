package com.parqueamestapp.services;

import com.parqueamestapp.model.Usuario;
import com.parqueamestapp.persistence.UsuarioRepository;
import com.parqueamestapp.util.PasswordPolicy;

import java.util.Optional;

// Perfil del usuario en sesion: editar nombre y cambiar contrasena.
public class PerfilService {

    // Resultado del cambio de contrasena (el JSP abre un modal segun el caso).
    public enum ResultadoCambio { OK, ACTUAL_INCORRECTA, IGUAL_A_ANTERIOR, NO_CUMPLE }

    private final UsuarioRepository repo = new UsuarioRepository();

    public Optional<Usuario> actualizarNombre(String cedula, String nombre) {
        if (cedula == null || nombre == null || nombre.isBlank() || nombre.length() < 6) return Optional.empty();
        if (!repo.update(cedula, nombre)) return Optional.empty();
        return repo.get(cedula);
    }

    public ResultadoCambio cambiarContrasena(String cedula, String actual, String nueva) {
        if (!PasswordPolicy.valida(nueva)) return ResultadoCambio.NO_CUMPLE;
        if (repo.autenticar(cedula, actual).isEmpty()) return ResultadoCambio.ACTUAL_INCORRECTA;
        if (nueva.equals(actual)) return ResultadoCambio.IGUAL_A_ANTERIOR;
        return repo.actualizarContrasena(cedula, nueva) ? ResultadoCambio.OK : ResultadoCambio.NO_CUMPLE;
    }
}
