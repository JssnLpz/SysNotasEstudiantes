package dataAccess;

import entities.Inscripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAL {
    private Connection conexion;

    public InscripcionDAL(Connection conexion) {
        this.conexion = conexion;
    }

    // Crear
    public boolean insertar(Inscripcion inscripcion) throws SQLException {
        String sql = "INSERT INTO Inscripcion (idUsuario, idEstudiante, idCurso, nota, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion.getIdUsuario());
            stmt.setInt(2, inscripcion.getIdEstudiante());
            stmt.setInt(3, inscripcion.getIdCurso());
            stmt.setInt(4, inscripcion.getNota());
            stmt.setInt(5, inscripcion.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    // Leer por ID
    public Inscripcion obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Inscripcion WHERE idInscripcion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Inscripcion(
                            rs.getInt("idInscripcion"),
                            rs.getInt("idUsuario"),
                            rs.getInt("idEstudiante"),
                            rs.getInt("idCurso"),
                            rs.getInt("nota"),
                            rs.getInt("estado")
                    );
                }
            }
        }
        return null;
    }

    // Leer todos
    public List<Inscripcion> obtenerTodos() throws SQLException {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Inscripcion";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Inscripcion(
                        rs.getInt("idInscripcion"),
                        rs.getInt("idUsuario"),
                        rs.getInt("idEstudiante"),
                        rs.getInt("idCurso"),
                        rs.getInt("nota"),
                        rs.getInt("estado")
                ));
            }
        }
        return lista;
    }

    // Actualizar
    public boolean actualizar(Inscripcion inscripcion) throws SQLException {
        String sql = "UPDATE Inscripcion SET idUsuario = ?, idEstudiante = ?, idCurso = ?, nota = ?, estado = ? WHERE idInscripcion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion.getIdUsuario());
            stmt.setInt(2, inscripcion.getIdEstudiante());
            stmt.setInt(3, inscripcion.getIdCurso());
            stmt.setInt(4, inscripcion.getNota());
            stmt.setInt(5, inscripcion.getEstado());
            stmt.setInt(6, inscripcion.getIdInscripcion());
            return stmt.executeUpdate() > 0;
        }
    }

    // Eliminar
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Inscripcion WHERE idInscripcion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}

