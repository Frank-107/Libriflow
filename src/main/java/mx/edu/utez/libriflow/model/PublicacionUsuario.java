package mx.edu.utez.libriflow.model;

/**
 * Clase que representa la entidad del modelo para las publicaciones de libros
 * creadas por los usuarios de la plataforma LibriFlow.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
public class PublicacionUsuario {

    /** Identificador único de la publicación del usuario. */
    private int idPublicacionUs;

    /** Identificador único del usuario propietario de la publicación. */
    private int idUsuario;

    /** Identificador único del libro asociado a la publicación. */
    private int idLibro;

    /** Fecha en la que fue creada o enviada la publicación. */
    private String fechaPublicacion;

    /** Estado actual de la publicación (ej. 'PENDIENTE', 'ACTIVO', 'RECHAZADO', 'VENDIDO'). */
    private String estado;

    /** Precio asignado a la venta del libro. */
    private double precio;

    /** Resumen o sinopsis del libro publicado. */
    private String sinopsis;

    /**
     * Constructor por defecto.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public PublicacionUsuario() {
    }

    /**
     * Obtiene la sinopsis del libro.
     *
     * @return Texto de la sinopsis.
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * Asigna la sinopsis del libro.
     *
     * @param sinopsis Texto de la sinopsis a establecer.
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * Obtiene el identificador de la publicación del usuario.
     *
     * @return Identificador entero de la publicación.
     */
    public int getIdPublicacionUs() {
        return idPublicacionUs;
    }

    /**
     * Establece el identificador de la publicación del usuario.
     *
     * @param idPublicacionUs Identificador entero a asignar.
     */
    public void setIdPublicacionUs(int idPublicacionUs) {
        this.idPublicacionUs = idPublicacionUs;
    }

    /**
     * Obtiene el ID del usuario propietario.
     *
     * @return Identificador del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Asigna el ID del usuario propietario.
     *
     * @param idUsuario Identificador único del usuario.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el ID del libro asociado.
     *
     * @return Identificador del libro.
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     * Asigna el ID del libro asociado.
     *
     * @param idLibro Identificador del libro.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     * Obtiene la fecha de creación de la publicación.
     *
     * @return Cadena con la fecha de publicación.
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de creación de la publicación.
     *
     * @param fechaPublicacion Cadena con la fecha.
     */
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene el estado de la publicación.
     *
     * @return Cadena con el estado actual.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado de la publicación.
     *
     * @param estado Nuevo estado a asignar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el precio de venta asignado.
     *
     * @return Precio en formato numérico decimal (`double`).
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Asigna el precio de venta del libro.
     *
     * @param precio Monto numérico decimal.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}