package com.parqueamesta.services;

import com.parqueamesta.model.Usuario;
import com.parqueamesta.persistence.UsuarioRepository;
import com.parqueamesta.util.PasswordPolicy;

import java.util.Optional;

// Perfil del usuario en sesion: editar nombre y cambiar contrasena.
public class PerfilService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public Optional<Usuario> actualizarNombre(String cedula, String nombre) {
        if (cedula == null || nombre == null || nombre.isBlank() || nombre.length() < 6) return Optional.empty();
        if (!repo.update(cedula, nombre)) return Optional.empty();
        return repo.get(cedula);
    }

    public boolean cambiarContrasena(String cedula, String actual, String nueva) {
        if (!PasswordPolicy.valida(nueva)) return false;
        if (repo.autenticar(cedula, actual).isEmpty()) return false;
        return repo.actualizarContrasena(cedula, nueva);
    }
}
