package com.parqueamestapp.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record DB() {
    // La app usa demo_parquadero; los tests apuntan a demo_parquadero_test
    // (lo define el plugin surefire en el pom.xml) para no borrar los datos reales.
    public static final String DB_URL =
            System.getProperty("db.url", "jdbc:postgresql://localhost:5432/demo_parquadero");
    public static final String DB_USER = System.getProperty("db.user", "postgres");
    public static final String DB_PASSWORD = System.getProperty("db.password", "ArqTests262");

    // En un contenedor (Tomcat) el driver va en WEB-INF/lib y DriverManager no lo
    // auto-registra, por eso lo cargamos explicitamente.
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
