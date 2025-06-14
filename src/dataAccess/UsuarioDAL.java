package dataAccess;

import entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAL {
    private Connection conexion;

    public UsuarioDAL(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar
    public boolean insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (Nombre, Telefono, Clave, Estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTelefono());
            stmt.setString(3, usuario.getClave());
            stmt.setInt(4, usuario.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    // Obtener por ID
    public Usuario obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    // Obtener todos
    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }
        return lista;
    }

    // Editar
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET Nombre = ?, Telefono = ?, Clave = ?, Estado = ? WHERE idUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTelefono());
            stmt.setString(3, usuario.getClave());
            stmt.setInt(4, usuario.getEstado());
            stmt.setInt(5, usuario.getIdUsuario());
            return stmt.executeUpdate() > 0;
        }
    }

    // Eliminar
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Buscar por teléfono (para login)
    public Usuario obtenerPorTelefono(String telefono) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE Telefono = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, telefono);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("idUsuario"),
                rs.getString("Nombre"),
                rs.getString("Clave"),
                rs.getString("Telefono"),
                rs.getInt("Estado")
        );
    }
}
