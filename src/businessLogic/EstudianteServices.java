package businessLogic;

import dataAccess.EstudianteDAL;
import entities.Estudiante;
import java.sql.SQLException;
import java.util.List;

public class EstudianteServices {
    private EstudianteDAL estudianteDAL;

    public EstudianteServices(EstudianteDAL estudianteDAL) {
        this.estudianteDAL = estudianteDAL;
    }

    public boolean insertarEstudiante(Estudiante estudiante) throws SQLException {
        if (estudiante.getNombre() == null || estudiante.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (!TelefonoValido(estudiante.getTelefono())) {
            throw new IllegalArgumentException("El teléfono solo puede contener números");
        }
        if (estudianteDAL.telefonoExiste(estudiante.getTelefono())) {
            throw new IllegalArgumentException("El número de teléfono ya está registrado");
        }
        return estudianteDAL.insertar(estudiante);
    }

    public Estudiante obtenerEstudiantePorId(int id) throws SQLException {
        return estudianteDAL.obtenerPorId(id);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() throws SQLException {
        return estudianteDAL.obtenerTodos();
    }

    public boolean actualizarEstudiante(Estudiante estudiante) throws SQLException {
        if (estudiante.getIdEstudiante() <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido");
        }
        if (!TelefonoValido(estudiante.getTelefono())) {
            throw new IllegalArgumentException("El teléfono solo puede contener números");
        }

        Estudiante existente = estudianteDAL.obtenerPorId(estudiante.getIdEstudiante());
        if (existente != null && !existente.getTelefono().equals(estudiante.getTelefono())) {
            if (estudianteDAL.telefonoExiste(estudiante.getTelefono())) {
                throw new IllegalArgumentException("El número de teléfono ya está registrado");
            }
        }
        return estudianteDAL.actualizar(estudiante);
    }

    public boolean eliminarEstudiante(int id) throws SQLException {
        return estudianteDAL.eliminar(id);
    }

    private boolean TelefonoValido(String telefono) {
        return telefono.matches("\\d+");
    }
}
