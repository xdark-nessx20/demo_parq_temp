package com.parqueamestapp.persistence;

import com.parqueamestapp.model.Operador;
import com.parqueamestapp.model.Rol;
import com.parqueamestapp.persistence.utils.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioRepositoryTest {

    private final UsuarioRepository repo = new UsuarioRepository();

    @BeforeEach
    void limpiar() throws SQLException {
        try (var connection = DB.conectar(); var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM pago");
            statement.executeUpdate("DELETE FROM registro_ingreso");
            statement.executeUpdate("DELETE FROM vehiculos");
            statement.executeUpdate("DELETE FROM usuarios");
        }
    }

    // Cédula corta (la columna es varchar(20)) y única por ejecución.
    private String cedulaUnica() {
        return String.valueOf(System.nanoTime()).substring(0, 15);
    }

    @Test
    void guardaYAutenticaOperador() {
        var cedula = cedulaUnica();
        assertTrue(repo.save(new Operador("Operador Test", cedula), "secreta123"));

        var autenticado = repo.autenticar(cedula, "secreta123");
        assertTrue(autenticado.isPresent());
        assertEquals(Rol.OPERADOR, autenticado.get().rol());
    }

    @Test
    void rechazaContrasenaIncorrecta() {
        var cedula = cedulaUnica();
        repo.save(new Operador("Operador Test", cedula), "secreta123");

        assertTrue(repo.autenticar(cedula, "otra-clave").isEmpty());
    }

    @Test
    void noGuardaContrasenaEnTextoPlano() throws SQLException {
        var cedula = cedulaUnica();
        repo.save(new Operador("Operador Test", cedula), "secreta123");

        try (var connection = DB.conectar();
             var st = connection.prepareStatement("SELECT contrasena_hash FROM usuarios WHERE cedula = ?")) {
            st.setString(1, cedula);
            try (var rs = st.executeQuery()) {
                rs.next();
                var hash = rs.getString("contrasena_hash");
                assertNotEquals("secreta123", hash);
                assertTrue(hash.contains(":"));
            }
        }
    }
}
