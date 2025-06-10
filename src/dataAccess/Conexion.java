package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:sqlserver://BDSecurity2025.mssql.somee.com:1433;"
            + "databaseName=BDSecurity2025;TrustServerCertificate=True;";
    private static final String USER = "AlfredoRamos_SQLLogin_2";
    private static final String PASSWORD = "hw2r1wtflq";

    public static Connection getConnection() throws SQLException {
        // Carga explícita del driver (opcional, pero puede ayudar)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
