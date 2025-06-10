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

    // Crear
    public boolean insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (Usuario, Nombre, Telefono, Clave, Estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getClave());
            stmt.setInt(5, usuario.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    // Leer (uno por ID)
    public Usuario obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE Usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("Usuario"),
                            rs.getString("Nombre"),
                            rs.getString("Telefono"),
                            rs.getString("Clave"),
                            rs.getInt("Estado")
                    );
                }
            }
        }
        return null;
    }

    // Leer (todos)
    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("Usuario"),
                        rs.getString("Nombre"),
                        rs.getString("Telefono"),
                        rs.getString("Clave"),
                        rs.getInt("Estado")
                ));
            }
        }
        return lista;
    }

    // Actualizar
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET Nombre = ?, Telefono = ?, Clave = ?, Estado = ? WHERE Usuario = ?";
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
        String sql = "DELETE FROM Usuario WHERE Usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}