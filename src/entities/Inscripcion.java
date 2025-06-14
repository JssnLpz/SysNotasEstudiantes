package entities;

public class Inscripcion {
    private int idInscripcion;
    private int idUsuario;
    private int idEstudiante;
    private int idCurso;
    private int nota;
    private int estado;

    public Inscripcion() {
    }

    // Constructor corregido: el orden de parámetros debe coincidir con la asignación
    public Inscripcion(int idInscripcion, int idUsuario, int idEstudiante, int idCurso, int nota, int estado) {
        this.idInscripcion = idInscripcion;
        this.idUsuario = idUsuario;
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;  // CORRECTO: idCurso va antes que nota
        this.nota = nota;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
