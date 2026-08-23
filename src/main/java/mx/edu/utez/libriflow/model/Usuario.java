package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase modelo que representa un usuario dentro del sistema LibriFlow.
 * Contiene información personal, datos de contacto, credenciales, estado de la cuenta y registros de tiempo del sistema.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @since 22/08/2026
 */
public class Usuario {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String telefono;
    private int id;
    private String contrasenaHash;
    private String estado;
    Timestamp fechaDesbloqueo;
    Timestamp fechaCreacion;

    /**
     * Constructor con parámetros para el registro inicial o creación de un usuario.
     *
     * @param nombre Nombre(s) del usuario.
     * @param apellidoPaterno Apellido paterno del usuario.
     * @param apellidoMaterno Apellido materno del usuario.
     * @param correo Correo electrónico del usuario.
     * @param telefono Número telefónico de contacto.
     * @param contrasena Contraseña o hash de la contraseña del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String telefono, String contrasena) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenaHash = contrasena;
    }

    /**
     * Constructor por defecto sin parámetros.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Usuario() {
    }

    /**
     * Obtiene el estado actual de la cuenta del usuario.
     *
     * @return Estado del usuario (ej. 'ACTIVO', 'BLOQUEADO').
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual de la cuenta del usuario.
     *
     * @param estado El nuevo estado para el usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El id del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id El nuevo id para el usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el número telefónico del usuario.
     *
     * @return El teléfono del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número telefónico del usuario.
     *
     * @param telefono El nuevo teléfono de contacto.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nuevo nombre del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    /**
     * Obtiene el apellido paterno del usuario.
     *
     * @return El apellido paterno.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Establece el apellido paterno del usuario.
     *
     * @param apellidoPaterno El nuevo apellido paterno.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Obtiene el apellido materno del usuario.
     *
     * @return El apellido materno.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Establece el apellido materno del usuario.
     *
     * @param apellidoMaterno El nuevo apellido materno.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo del usuario.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param correo El nuevo correo electrónico.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña encriptada/hash del usuario.
     *
     * @return El hash de la contraseña.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getContrasenaHash() {
        return contrasenaHash;
    }

    /**
     * Establece la contraseña encriptada/hash del usuario.
     *
     * @param contrasena La nueva contraseña o hash.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setContrasenaHash(String contrasena) {
        this.contrasenaHash = contrasena;
    }

    /**
     * Obtiene la fecha y hora estimada de desbloqueo de la cuenta en caso de haber sido bloqueada.
     *
     * @return La marca de tiempo (Timestamp) de desbloqueo.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Timestamp getFechaDesbloqueo() {
        return fechaDesbloqueo;
    }

    /**
     * Establece la fecha y hora de desbloqueo de la cuenta del usuario.
     *
     * @param fechaDesbloqueo La marca de tiempo del desbloqueo.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setFechaDesbloqueo(Timestamp fechaDesbloqueo) {
        this.fechaDesbloqueo = fechaDesbloqueo;
    }

    /**
     * Obtiene la fecha y hora de creación de la cuenta en el sistema.
     *
     * @return La marca de tiempo (Timestamp) de creación.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación de la cuenta del usuario.
     *
     * @param fechaCreacion La marca de tiempo de creación.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}