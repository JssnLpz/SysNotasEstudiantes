package businessLogic;

import dataAccess.CursoDAL;
import entities.Curso;

import java.sql.SQLException;
import java.util.List;

public class CursoServices {
    private CursoDAL cursoDAL;

    public CursoServices(CursoDAL cursoDAL) {
        this.cursoDAL = cursoDAL;
    }

    // Obtener todos los cursos
    public List<Curso> obtenerTodos() throws SQLException {
        return cursoDAL.obtenerTodos();
    }

    // Obtener curso por ID
    public Curso obtenerPorId(int id) throws SQLException {
        return cursoDAL.obtenerPorId(id);
    }

    // Insertar nuevo curso con validaciones
    public boolean insertar(Curso curso) throws SQLException, IllegalArgumentException {
        validarCurso(curso);

        // Validar que no exista otro curso con el mismo nombre
        List<Curso> cursos = cursoDAL.obtenerTodos();
        for (Curso c : cursos) {
            if (c.getNombre().equalsIgnoreCase(curso.getNombre())) {
                throw new IllegalArgumentException("Ya existe un curso con el mismo nombre.");
            }
        }

        return cursoDAL.insertar(curso);
    }

    // Actualizar curso con validaciones
    public boolean actualizar(Curso curso) throws SQLException, IllegalArgumentException {
        validarCurso(curso);

        // Validar que no exista otro curso con el mismo nombre diferente al actual
        List<Curso> cursos = cursoDAL.obtenerTodos();
        for (Curso c : cursos) {
            if (c.getNombre().equalsIgnoreCase(curso.getNombre()) && c.getIdCurso() != curso.getIdCurso()) {
                throw new IllegalArgumentException("Ya existe otro curso con el mismo nombre.");
            }
        }

        return cursoDAL.actualizar(curso);
    }

    // Eliminar curso
    public boolean eliminar(int id) throws SQLException {
        return cursoDAL.eliminar(id);
    }

    // Validación básica de datos
    private void validarCurso(Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio.");
        }
        if (curso.getDescripcion() == null) {
            curso.setDescripcion(""); // Permitir descripción vacía, si se quiere.
        }
    }
}