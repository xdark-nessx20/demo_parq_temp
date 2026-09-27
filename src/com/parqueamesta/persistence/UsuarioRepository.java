package com.parqueamesta.persistence;

import com.parqueamesta.model.Cliente;
import com.parqueamesta.model.Gerente;
import com.parqueamesta.model.Operador;
import com.parqueamesta.model.Rol;
import com.parqueamesta.model.Usuario;
import com.parqueamesta.persistence.utils.DB;
import com.parqueamesta.persistence.utils.PasswordHasher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Repositorio unico de personas: la tabla "usuarios" guarda clientes, operadores y gerentes,
// diferenciados por la columna "rol".
public record UsuarioRepository() {

    public boolean save(Usuario usuario, String contrasenaPlano) {
        var query = "INSERT INTO usuarios (nombre, cedula, rol, contrasena_hash) VALUES (?, ?, ?, ?)";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, usuario.nombre());
            statement.setString(2, usuario.cedula());
            statement.setString(3, usuario.rol().name());
            statement.setString(4, contrasenaPlano == null ? null : PasswordHasher.hash(contrasenaPlano));

            int affectedRows = statement.executeUpdate();
            statement.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Usuario> get(String cedula) {
        var query = "SELECT id, nombre, cedula, rol FROM usuarios WHERE cedula = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, cedula);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(construir(result));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> getByRol(Rol rol) {
        var query = "SELECT id, nombre, cedula, rol FROM usuarios WHERE rol = ?";
        var usuarios = new ArrayList<Usuario>();

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, rol.name());

            try (var result = statement.executeQuery()) {
                while (result.next()) {
                    usuarios.add(construir(result));
                }
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> getAll() {
        var query = "SELECT id, nombre, cedula, rol FROM usuarios";
        var usuarios = new ArrayList<Usuario>();

        try (var connection = DB.conectar(); var statement = connection.prepareStatement(query);
             var result = statement.executeQuery()) {
            while (result.next()) {
                usuarios.add(construir(result));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Verifica cedula + contraseña contra el hash almacenado. Devuelve el usuario si son validas.
    public Optional<Usuario> autenticar(String cedula, String contrasenaPlano) {
        if (cedula == null || contrasenaPlano == null) return Optional.empty();

        var query = "SELECT id, nombre, cedula, rol, contrasena_hash FROM usuarios WHERE cedula = ?";

        try (var connection = DB.conectar()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, cedula);

            try (var result = statement.executeQuery()) {
                if (result.next() && PasswordHasher.verificar(contrasenaPlano, result.getString("contrasena_hash"))) {
                    return Optional.of(construir(result));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Crea la subclase correcta (Cliente/Operador/Gerente) segun el rol de la fila.
    private Usuario construir(java.sql.ResultSet result) throws SQLException {
        var id = result.getObject("id", UUID.class);
        var nombre = result.getString("nombre");
        var cedula = result.getString("cedula");
        return switch (Rol.valueOf(result.getString("rol"))) {
            case CLIENTE -> new Cliente(id, nombre, cedula);
            case OPERADOR -> new Operador(id, nombre, cedula);
            case GERENTE -> new Gerente(id, nombre, cedula);
        };
    }
}
