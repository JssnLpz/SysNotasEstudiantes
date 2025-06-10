package entities;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private  String clave;
    private String telefono;
    private int estado;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String clave, String telefono, int estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.clave = clave;
        this.telefono = telefono;
        this.estado = estado;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
