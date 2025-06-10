package entities;

public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String telefono;
    private int estado;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String nombre, String telefono, int estado) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.telefono = telefono;
        this.estado = estado;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

