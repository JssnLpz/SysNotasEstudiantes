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
        validarCampos(usuario);
        if (telefonoExiste(usuario.getTelefono())) {
            throw new Exception("El teléfono ya está registrado.");
        }
        usuario.setClave(encriptarSHA256(usuario.getClave()));
        return dao.insertar(usuario);
    }

    public boolean actualizar(Usuario usuario) throws Exception {
        validarCampos(usuario);
        Usuario existente = obtenerPorTelefono(usuario.getTelefono());
        if (existente != null && existente.getIdUsuario() != usuario.getIdUsuario()) {
            throw new Exception("El teléfono ya está registrado.");
        }
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

    private boolean telefonoExiste(String telefono) throws SQLException {
        Usuario usuario = obtenerPorTelefono(telefono);
        return usuario != null;
    }

    private Usuario obtenerPorTelefono(String telefono) throws SQLException {
        return dao.obtenerPorTelefono(telefono);
    }

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

    private void validarCampos(Usuario usuario) throws Exception {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty())
            throw new Exception("El nombre no puede estar vacío.");
        if (usuario.getClave() == null || usuario.getClave().trim().isEmpty())
            throw new Exception("La clave no puede estar vacía.");
        if (usuario.getClave().length() < 6)
            throw new Exception("La clave debe tener al menos 6 caracteres.");
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty())
            throw new Exception("El teléfono no puede estar vacío.");
        if (!usuario.getTelefono().matches("\\d+"))
            throw new Exception("El teléfono solo puede contener números.");
    }

    // Validaciones para el login
    public Usuario login(String telefono, String clave) throws Exception {
        Usuario usuario = dao.obtenerPorTelefono(telefono);
        if (usuario == null) {
            throw new Exception("El teléfono no está registrado.");
        }
        if (usuario.getEstado() != 1) {
            throw new Exception("El usuario está inactivo.");
        }
        String claveEncriptada = encriptarSHA256(clave);
        if (!usuario.getClave().equals(claveEncriptada)) {
            throw new Exception("La clave es incorrecta.");
        }
        return usuario;
    }
}
