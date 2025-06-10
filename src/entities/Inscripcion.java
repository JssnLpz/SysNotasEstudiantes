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

    public Inscripcion(int idInscripcion, int idUsuario, int idEstudiante, int nota, int idCurso, int estado) {
        this.idInscripcion = idInscripcion;
        this.idUsuario = idUsuario;
        this.idEstudiante = idEstudiante;
        this.nota = nota;
        this.idCurso = idCurso;
        this.estado = estado;
    }

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
