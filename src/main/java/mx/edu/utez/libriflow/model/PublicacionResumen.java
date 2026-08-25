package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;

/**
 * Clase DTO/Modelo que representa una vista resumida de una publicación en la plataforma LibriFlow.
 * Diseñada para alimentar vistas de catálogos, tarjetas de productos, listas de búsquedas y elementos del carrito de compras.
 *
 * @author Monserrath Anzurez
 * @since 23/08/26
 */
public class PublicacionResumen {

    /** Identificador único de la publicación. */
    private int idPublicacion;

    /** Título del libro. */
    private String titulo;

    /** Nombre del autor del libro. */
    private String autor;

    /** Género literario del libro. */
    private String genero;

    /** Precio de venta o precio base del ejemplar. */
    private Double precio;

    /** Cadena base64 o ruta con la imagen principal del libro. */
    private String imagenPrincipal;

    /** Estado actual de la publicación (ej. 'ACTIVO', 'PENDIENTE', 'RECHAZADO'). */
    private String estado;

    /** Nombre del usuario o propietario que realizó la publicación. */
    private String nombrePropietario;

    /** Identificador único del propietario de la publicación. */
    private int idPropietario;

    /** Bandera que indica si la publicación pertenece formalmente a LibriFlow (`true`) o a un usuario (`false`). */
    private boolean esLibriFlow;

    /** Bandera que indica si el usuario seleccionó la modalidad de renta para este elemento en el carrito/pedido. */
    private boolean esRentaSeleccionada;

    /** Monto del precio calculado específicamente para el periodo de renta. */
    private Double precioRentaCalculado;

    /** Cantidad de ejemplares seleccionados. */
    private int cantidad;

    /** Fecha de inicio estipulada para el periodo de renta. */
    private Timestamp fechaInicio;

    /** Fecha de finalización estipulada para el periodo de renta. */
    private Timestamp fechaFin;

    /**
     * Constructor por defecto.
     *
     * @author Monserrath Anzurez
     * @since 23/08/26
     */
    public PublicacionResumen() {
    }

    /**
     * Obtiene la cantidad de ejemplares.
     *
     * @return Cantidad entera.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de ejemplares.
     *
     * @param cantidad Cantidad entera a asignar.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el identificador del propietario de la publicación.
     *
     * @return Identificador del propietario.
     */
    public int getIdPropietario() {
        return idPropietario;
    }

    /**
     * Asigna el identificador del propietario de la publicación.
     *
     * @param idPropietario Identificador único del usuario propietario.
     */
    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    /**
     * Indica si la publicación proviene directamente del inventario oficial de LibriFlow.
     *
     * @return `true` si es de LibriFlow; `false` si proviene de un usuario externo.
     */
    public boolean isEsLibriFlow() {
        return esLibriFlow;
    }

    /**
     * Define el origen de la publicación (LibriFlow o Usuario).
     *
     * @param esLibriFlow Valor booleano.
     */
    public void setEsLibriFlow(boolean esLibriFlow) {
        this.esLibriFlow = esLibriFlow;
    }

    /**
     * Obtiene la imagen principal de la publicación.
     *
     * @return Cadena con la representación de la imagen.
     */
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    /**
     * Asigna la imagen principal de la publicación.
     *
     * @param imagenPrincipal Cadena con la imagen.
     */
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    /**
     * Obtiene el precio base o de venta.
     *
     * @return Precio en formato {@link Double}.
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del libro.
     *
     * @param precio Precio en formato {@link Double}.
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el género literario.
     *
     * @return Nombre del género.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Asigna el género literario.
     *
     * @param genero Nombre del género.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return Nombre del autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Asigna el autor del libro.
     *
     * @param autor Nombre del autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return Título del libro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Asigna el título del libro.
     *
     * @param titulo Título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el ID único de la publicación.
     *
     * @return Identificador de la publicación.
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Asigna el ID único de la publicación.
     *
     * @param idPublicacion Identificador entero.
     */
    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    /**
     * Obtiene el estado actual de la publicación.
     *
     * @return Cadena con el estado.
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
     * Obtiene el nombre del propietario de la publicación.
     *
     * @return Nombre del propietario.
     */
    public String getNombrePropietario() {
        return nombrePropietario;
    }

    /**
     * Establece el nombre del propietario.
     *
     * @param nombrePropietario Nombre del propietario.
     */
    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    /**
     * Indica si la modalidad seleccionada por el cliente es de renta.
     *
     * @return `true` si se seleccionó renta; `false` si es compra/venta.
     */
    public boolean isEsRentaSeleccionada() {
        return esRentaSeleccionada;
    }

    /**
     * Establece la bandera de modalidad de renta seleccionada.
     *
     * @param esRentaSeleccionada Valor booleano.
     */
    public void setEsRentaSeleccionada(boolean esRentaSeleccionada) {
        this.esRentaSeleccionada = esRentaSeleccionada;
    }

    /**
     * Obtiene el precio calculado para el periodo de renta.
     *
     * @return Monto en formato {@link Double}.
     */
    public Double getPrecioRentaCalculado() {
        return precioRentaCalculado;
    }

    /**
     * Asigna el precio calculado para el periodo de renta.
     *
     * @param precioRentaCalculado Monto en formato {@link Double}.
     */
    public void setPrecioRentaCalculado(Double precioRentaCalculado) {
        this.precioRentaCalculado = precioRentaCalculado;
    }

    /**
     * Obtiene la fecha de inicio de la renta.
     *
     * @return Marca de tiempo {@link Timestamp} inicial.
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Define la fecha de inicio de la renta.
     *
     * @param fechaInicio Marca de tiempo {@link Timestamp} inicial.
     */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha límite o de entrega de la renta.
     *
     * @return Marca de tiempo {@link Timestamp} final.
     */
    public Timestamp getFechaFin() {
        return fechaFin;
    }

    /**
     * Define la fecha límite o de entrega de la renta.
     *
     * @param fechaFin Marca de tiempo {@link Timestamp} final.
     */
    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }
}