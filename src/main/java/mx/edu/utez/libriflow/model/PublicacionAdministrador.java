package mx.edu.utez.libriflow.model;

/**
 * Clase que representa la entidad del modelo para las publicaciones gestionadas
 * directamente por los administradores de la plataforma LibriFlow.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
public class PublicacionAdministrador {

    /** Identificador único de la publicación de LibriFlow. */
    private int idPublicacionLf;

    /** Identificador del libro asociado a la publicación. */
    private int idLibro;

    /** Fecha en la que se realizó la publicación. */
    private String fechaPublicacion;

    /** Estado actual de la publicación (ej. 'ACTIVO', 'INACTIVO'). */
    private String estado;

    /** Cantidad disponible en stock para venta o renta. */
    private int cantidad;

    /** Resumen o sinopsis del libro publicado. */
    private String sinopsis;

    /** Indicador para señalar si la publicación permite venta (1 = Sí, 0 = No). */
    private int esVenta;

    /** Indicador para señalar si la publicación permite renta (1 = Sí, 0 = No). */
    private int esRenta;

    /** Precio asignado al libro en la publicación. */
    private double precio;

    /**
     * Constructor por defecto.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public PublicacionAdministrador() {
    }

    /**
     * Constructor sobrecargado para inicializar todos los campos de la publicación.
     *
     * @param idPublicacionLf Identificador único de la publicación LibriFlow.
     * @param idLibro Identificador del libro.
     * @param fechaPublicacion Fecha de la publicación.
     * @param estado Estado de la publicación.
     * @param cantidad Stock disponible.
     * @param sinopsis Sinopsis del libro.
     * @param esVenta Indicador de venta.
     * @param esRenta Indicador de renta.
     * @param precio Precio del ejemplar.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public PublicacionAdministrador(int idPublicacionLf, int idLibro, String fechaPublicacion, String estado, int cantidad, String sinopsis, int esVenta, int esRenta, double precio) {
        this.idPublicacionLf = idPublicacionLf;
        this.idLibro = idLibro;
        this.fechaPublicacion = fechaPublicacion;
        this.estado = estado;
        this.cantidad = cantidad;
        this.sinopsis = sinopsis;
        this.esVenta = esVenta;
        this.esRenta = esRenta;
        this.precio = precio;
    }

    /**
     * Obtiene el identificador de la publicación.
     *
     * @return Identificador entero de la publicación.
     */
    public int getIdPublicacionLf() {
        return idPublicacionLf;
    }

    /**
     * Establece el identificador de la publicación.
     *
     * @param idPublicacionLf Nuevo identificador entero.
     */
    public void setIdPublicacionLf(int idPublicacionLf) {
        this.idPublicacionLf = idPublicacionLf;
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
     * @param idLibro Nuevo ID del libro.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     * Obtiene la fecha de publicación.
     *
     * @return Cadena con la fecha.
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicación.
     *
     * @param fechaPublicacion Nueva fecha.
     */
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene el estado actual de la publicación.
     *
     * @return Estado de la publicación.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado de la publicación.
     *
     * @param estado Nuevo estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la cantidad de stock disponible.
     *
     * @return Cantidad entera.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Define la cantidad de stock disponible.
     *
     * @param cantidad Nueva cantidad.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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
     * @param sinopsis Nueva sinopsis.
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * Obtiene la bandera que indica si la publicación está disponible para venta.
     *
     * @return Valor entero (1 = Venta habilitada, 0 = No habilitada).
     */
    public int getEsVenta() {
        return esVenta;
    }

    /**
     * Establece el indicador de venta.
     *
     * @param esVenta Valor entero.
     */
    public void setEsVenta(int esVenta) {
        this.esVenta = esVenta;
    }

    /**
     * Obtiene la bandera que indica si la publicación está disponible para renta.
     *
     * @return Valor entero (1 = Renta habilitada, 0 = No habilitada).
     */
    public int getEsRenta() {
        return esRenta;
    }

    /**
     * Establece el indicador de renta.
     *
     * @param esRenta Valor entero.
     */
    public void setEsRenta(int esRenta) {
        this.esRenta = esRenta;
    }

    /**
     * Obtiene el precio asignado.
     *
     * @return Precio en formato numérico decimal (`double`).
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Asigna el precio del producto.
     *
     * @param precio Nuevo precio.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}