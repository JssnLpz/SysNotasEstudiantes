import dataAccess.Conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = Conexion.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡Conexión establecida correctamente!");
            }
        } catch (SQLException e) {
            System.out.println(" Error al conectar: " + e.getMessage());
        }
    }
}
