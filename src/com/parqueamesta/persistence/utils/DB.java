package com.parqueamesta.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record DB() {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/demo_parquadero";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "ArqTests262";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
