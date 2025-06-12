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
        String sql = "INSERT INTO Usuario (Nombre, Telefono, Clave, Estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTelefono());
            stmt.setString(3, usuario.getClave());
            stmt.setInt(4, usuario.getEstado());
            return stmt.executeUpdate() > 0;
        }
    }

    // Leer (uno por ID)
    public Usuario obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("idUsuario"),
                            rs.getString("Nombre"),
                            rs.getString("Clave"),     // <- CORREGIDO: primero la clave
                            rs.getString("Telefono"),  // <- luego el teléfono
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
                        rs.getInt("idUsuario"),
                        rs.getString("Nombre"),
                        rs.getString("Clave"),     // <- CORREGIDO
                        rs.getString("Telefono"),  // <- CORREGIDO
                        rs.getInt("Estado")
                ));
            }
        }
        return lista;
    }

    // Actualizar
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
    public Usuario autenticar(String nombre, String clave) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE Nombre = ? AND Clave = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, clave);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("idUsuario"),
                            rs.getString("Nombre"),
                            rs.getString("Clave"),
                            rs.getString("Telefono"),
                            rs.getInt("Estado")
                    );
                }
            }
        }
        return null;
    }

}

