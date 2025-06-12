package dataAccess;

import entities.Estudiante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAL {
    private Connection conexion;

    public EstudianteDAL(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean insertar(Estudiante estudiante) throws SQLException {
        String sql = "INSERT INTO Estudiante (Nombre, Telefono, Estado) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getTelefono());
            stmt.setInt(3, estudiante.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    public Estudiante obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Estudiante WHERE idEstudiante = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("idEstudiante"),
                            rs.getString("Nombre"),
                            rs.getString("Telefono"),
                            rs.getInt("Estado")
                    );
                }
            }
        }
        return null;
    }

    public List<Estudiante> obtenerTodos() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estudiante";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getInt("idEstudiante"),
                        rs.getString("Nombre"),
                        rs.getString("Telefono"),
                        rs.getInt("Estado")
                ));
            }
        }
        return lista;
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {
        String sql = "UPDATE Estudiante SET Nombre = ?, Telefono = ?, Estado = ? WHERE idEstudiante = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getTelefono());
            stmt.setInt(3, estudiante.getEstado());
            stmt.setInt(4, estudiante.getIdEstudiante());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Estudiante WHERE idEstudiante = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean telefonoExiste(String telefono) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Estudiante WHERE Telefono = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, telefono);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
