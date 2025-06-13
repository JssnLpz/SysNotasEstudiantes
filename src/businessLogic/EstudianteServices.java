package businessLogic;

import dataAccess.EstudianteDAL;
import entities.Estudiante;

import java.sql.SQLException;
import java.util.List;

public class EstudianteServices {
    private EstudianteDAL dao;

    public EstudianteServices(EstudianteDAL dao) {
        this.dao = dao;
    }

    public boolean insertar(Estudiante estudiante) throws Exception {
        if (telefonoExiste(estudiante.getTelefono())) {
            throw new Exception("El teléfono ya está registrado en otro estudiante.");
        }

        return dao.insertar(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) throws Exception {
        Estudiante existente = obtenerPorTelefono(estudiante.getTelefono());
        if (existente != null && existente.getIdEstudiante() != estudiante.getIdEstudiante()) {
            throw new Exception("El teléfono ya está registrado en otro estudiante.");
        }

        return dao.actualizar(estudiante);
    }

    public Estudiante obtenerPorId(int id) throws SQLException {
        return dao.obtenerPorId(id);
    }

    public List<Estudiante> obtenerTodos() throws SQLException {
        return dao.obtenerTodos();
    }

    public boolean eliminar(int id) throws SQLException {
        return dao.eliminar(id);
    }

    // Validación de teléfono único
    private boolean telefonoExiste(String telefono) throws SQLException {
        Estudiante estudiante = obtenerPorTelefono(telefono);
        return estudiante != null;
    }

    private Estudiante obtenerPorTelefono(String telefono) throws SQLException {
        List<Estudiante> lista = dao.obtenerTodos();
        for (Estudiante e : lista) {
            if (e.getTelefono().equals(telefono)) {
                return e;
            }
        }
        return null;
    }
}
