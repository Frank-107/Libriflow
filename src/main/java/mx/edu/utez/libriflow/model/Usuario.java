package mx.edu.utez.libriflow.model;

public class Usuario {
    private String nombre;
    private String apellidoPaterno;
    private String appellidoMaterno;
    private String correo;
    //temporalmente se va a guardar la contraseña aqui, luego se guardara en credenciales
    private String contrasena;

    public Usuario(String nombre, String apellidoPaterno, String appellidoMaterno, String correo, String contrasena) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.appellidoMaterno = appellidoMaterno;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getAppellidoMaterno() {
        return appellidoMaterno;
    }

    public void setAppellidoMaterno(String appellidoMaterno) {
        this.appellidoMaterno = appellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return nombre+","+
                apellidoPaterno+","+
                appellidoMaterno+","+
                correo+","+
                contrasena;
    }
}
