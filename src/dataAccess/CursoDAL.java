package dataAccess;

import entities.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAL {
    private Connection conexion;

    public CursoDAL(Connection conexion) {
        this.conexion = conexion;
    }

    // Crear
    public boolean insertar(Curso curso) throws SQLException {
        String sql = "INSERT INTO Curso (Nombre, Descripcion, Estado) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    // Leer por ID
    public Curso obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Curso WHERE idCurso = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Curso(
                            rs.getInt("idCurso"),
                            rs.getString("Nombre"),
                            rs.getString("Descripcion"),
                            rs.getInt("Estado")
                    );
                }
            }
        }
        return null;
    }

    // Leer todos
    public List<Curso> obtenerTodos() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM Curso";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Curso(
                        rs.getInt("idCurso"),
                        rs.getString("Nombre"),
                        rs.getString("Descripcion"),
                        rs.getInt("Estado")
                ));
            }
        }
        return lista;
    }

    // Actualizar
    public boolean actualizar(Curso curso) throws SQLException {
        String sql = "UPDATE Curso SET Nombre = ?, Descripcion = ?, Estado = ? WHERE idCurso = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getEstado());
            stmt.setInt(4, curso.getIdCurso());
            return stmt.executeUpdate() > 0;
        }
    }

    // Eliminar
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Curso WHERE idCurso = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}

