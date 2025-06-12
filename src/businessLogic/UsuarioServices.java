package businessLogic;

import dataAccess.UsuarioDAL;
import entities.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class UsuarioServices {
    private UsuarioDAL dao;

    public UsuarioServices(UsuarioDAL dao) {
        this.dao = dao;
    }

    public boolean insertar(Usuario usuario) throws Exception {
        // Validar teléfono único
        if (telefonoExiste(usuario.getTelefono())) {
            throw new Exception("El teléfono ya está registrado en otro usuario.");
        }

        // Encriptar la clave
        String claveEncriptada = encriptarSHA256(usuario.getClave());
        usuario.setClave(claveEncriptada);

        // Insertar en base de datos
        return dao.insertar(usuario);
    }

    public boolean actualizar(Usuario usuario) throws Exception {
        // Validar teléfono único (excepto el propio usuario)
        Usuario existente = obtenerPorTelefono(usuario.getTelefono());
        if (existente != null && existente.getIdUsuario() != usuario.getIdUsuario()) {
            throw new Exception("El teléfono ya está registrado en otro usuario.");
        }

        // Encriptar la clave solo si la cambió (esto depende de tu lógica)
        usuario.setClave(encriptarSHA256(usuario.getClave()));

        return dao.actualizar(usuario);
    }

    public Usuario obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        return dao.obtenerTodos();
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }

    // Método auxiliar: verificar si teléfono ya existe
    private boolean telefonoExiste(String telefono) throws SQLException {
        Usuario usuario = obtenerPorTelefono(telefono);
        return usuario != null;
    }

    private Usuario obtenerPorTelefono(String telefono) throws SQLException {
        List<Usuario> lista = dao.obtenerTodos();
        for (Usuario u : lista) {
            if (u.getTelefono().equals(telefono)) {
                return u;
            }
        }
        return null;
    }

    // Método para encriptar clave con SHA-256
    private String encriptarSHA256(String clave) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(clave.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
