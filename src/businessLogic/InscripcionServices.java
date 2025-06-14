package businessLogic;

import dataAccess.InscripcionDAL;
import entities.Inscripcion;

import java.sql.SQLException;
import java.util.List;

public class InscripcionServices {
    private InscripcionDAL inscripcionDAL;

    public InscripcionServices(InscripcionDAL inscripcionDAL) {
        this.inscripcionDAL = inscripcionDAL;
    }

    public void agregar(Inscripcion inscripcion) throws SQLException {
        validarInscripcion(inscripcion);
        inscripcionDAL.insertar(inscripcion);
    }

    public void actualizar(Inscripcion inscripcion) throws SQLException {
        validarInscripcion(inscripcion);
        inscripcionDAL.actualizar(inscripcion);
    }

    public void eliminar(int id) throws SQLException {
        inscripcionDAL.eliminar(id);
    }

    public List<Inscripcion> obtenerTodos() throws SQLException {
        return inscripcionDAL.obtenerTodos();
    }

    public Inscripcion obtenerPorId(int id) throws SQLException {
        return inscripcionDAL.obtenerPorId(id);
    }

    // Validación de inscripcion
    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getNota() < 0 || inscripcion.getNota() > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        }

        if (inscripcion.getEstado() != 0 && inscripcion.getEstado() != 1) {
            throw new IllegalArgumentException("El estado debe ser 0 (inactivo) o 1 (activo).");
        }

        // Validar claves foráneas obligatorias
        if (inscripcion.getIdUsuario() <= 0 || inscripcion.getIdEstudiante() <= 0 || inscripcion.getIdCurso() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar usuario, estudiante y curso.");
        }
    }
}
