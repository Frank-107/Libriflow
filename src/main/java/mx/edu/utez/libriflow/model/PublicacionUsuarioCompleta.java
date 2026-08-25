package mx.edu.utez.libriflow.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo (DTO/Entity) que representa la información completa e integral de una publicación
 * realizada por un usuario dentro del sistema LibriFlow.
 * Contiene los datos del libro, información de la publicación (precio, estado, sinopsis),
 * rutas de imágenes de evidencia y datos del usuario propietario.
 *
 * @author Francisco
 * @since 24/08/2026
 */
public class PublicacionUsuarioCompleta {

    /** Identificador único de la publicación. */
    private int idPublicacion;

    /** Identificador del usuario propietario de la publicación. */
    private int idPropietario;

    /** Identificador del libro asociado en la base de datos. */
    private int idLibro;

    /** Título del libro. */
    private String titulo;

    /** Autor del libro. */
    private String autor;

    /** Editorial del libro. */
    private String editorial;

    /** Género o categoría literaria del libro. */
    private String genero;

    /** Resumen o sinopsis del contenido del libro. */
    private String sinopsis;

    /** Precio de venta o valor asignado a la publicación. */
    private double precio;

    /** Estado actual de la publicación (ej. ACTIVO, INACTIVO, PENDIENTE). */
    private String estado;

    /** Fecha y hora exacta de creación de la publicación. */
    private LocalDateTime fecha;

    /** Indicador de modalidad de la transacción (1 para Venta, 0 para Renta). Por defecto es 1. */
    private int esVenta = 1;

    /** Ruta o URL de la imagen principal / portada del libro. */
    private String imagenPrincipal;

    /** Ruta o URL de la imagen del reverso / contraportada del libro. */
    private String imagenReverso;

    /** Ruta o URL de la imagen del interior o estado de las páginas del libro. */
    private String imagenInterior;

    /**
     * Constructor predeterminado por defecto.
     */
    public PublicacionUsuarioCompleta() {
    }

    /**
     * Obtiene la fecha y hora de la publicación.
     *
     * @return Objeto {@link LocalDateTime} con la fecha de registro.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora de registro de la publicación.
     *
     * @param fecha Objeto {@link LocalDateTime} con la nueva fecha.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID de la publicación.
     *
     * @return Entero con el identificador de la publicación.
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Establece el ID de la publicación.
     *
     * @param idPublicacion Nuevo identificador numérico de la publicación.
     */
    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    /**
     * Obtiene el ID del usuario propietario.
     *
     * @return Entero con el ID del propietario.
     */
    public int getIdPropietario() {
        return idPropietario;
    }

    /**
     * Establece el ID del usuario propietario.
     *
     * @param idPropietario Identificador del usuario creador.
     */
    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    /**
     * Obtiene el ID del libro asociado.
     *
     * @return Entero con el ID del libro.
     */
    public int getIdLibro() {
        return idLibro;
    }

    /**
     * Establece el ID del libro asociado.
     *
     * @param idLibro Identificador del libro.
     */
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return Cadena con el título.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo Cadena de texto con el título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return Cadena con el nombre del autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     *
     * @param autor Nombre del autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return Cadena con la editorial.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Establece la editorial del libro.
     *
     * @param editorial Nombre de la casa editorial.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene el género literario del libro.
     *
     * @return Cadena con el género.
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Establece el género literario del libro.
     *
     * @param genero Categoría o género literario.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene la sinopsis del libro.
     *
     * @return Cadena con la sinopsis del libro.
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * Establece la sinopsis o descripción del libro.
     *
     * @param sinopsis Resumen o sinopsis informativa.
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * Obtiene el precio de venta de la publicación.
     *
     * @return Valor numérico decimal con el precio.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta de la publicación.
     *
     * @param precio Monto económico asignado.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el estado actual de la publicación.
     *
     * @return Cadena con el estado de la publicación.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la publicación.
     *
     * @param estado Nuevo estado (ej. ACTIVO, INACTIVO).
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la ruta de la imagen principal.
     *
     * @return Cadena con la ruta de la imagen principal.
     */
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    /**
     * Establece la ruta de la imagen principal del libro.
     *
     * @param imagenPrincipal Ruta de acceso al archivo de imagen.
     */
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    /**
     * Obtiene la ruta de la imagen del reverso.
     *
     * @return Cadena con la ruta de la imagen del reverso.
     */
    public String getImagenReverso() {
        return imagenReverso;
    }

    /**
     * Establece la ruta de la imagen del reverso del libro.
     *
     * @param imagenReverso Ruta de acceso a la contraportada.
     */
    public void setImagenReverso(String imagenReverso) {
        this.imagenReverso = imagenReverso;
    }

    /**
     * Obtiene la ruta de la imagen interior.
     *
     * @return Cadena con la ruta de la imagen interior.
     */
    public String getImagenInterior() {
        return imagenInterior;
    }

    /**
     * Establece la ruta de la imagen del interior del libro.
     *
     * @param imagenInterior Ruta de acceso a la imagen de hojas/interior.
     */
    public void setImagenInterior(String imagenInterior) {
        this.imagenInterior = imagenInterior;
    }

    /**
     * Obtiene el indicador de si es una venta.
     *
     * @return Entero (1 para venta, 0 para renta).
     */
    public int getEsVenta() {
        return esVenta;
    }

    /**
     * Establece el indicador de tipo de transacción (venta/renta).
     *
     * @param esVenta Entero representando la modalidad.
     */
    public void setEsVenta(int esVenta) {
        this.esVenta = esVenta;
    }
}