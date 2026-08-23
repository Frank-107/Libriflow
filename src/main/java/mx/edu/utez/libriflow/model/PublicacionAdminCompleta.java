package mx.edu.utez.libriflow.model;

/**
 *
 * Esta clase representa la información completa de una publicación
 * administrada directamente por LibriFlow. Almacena los datos del libro,
 * información de la publicación, disponibilidad para venta o renta y las
 * imágenes relacionadas con la publicación.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
public class PublicacionAdminCompleta {

    /**
     *
     * Identificador de la publicación administrada por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int idPublicacionLf;

    /**
     *
     * Identificador del libro relacionado con la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int idLibro;

    /**
     *
     * Título del libro publicado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String titulo;

    /**
     *
     * Nombre del autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String autor;

    /**
     *
     * Editorial correspondiente al libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String editorial;

    /**
     *
     * Género al que pertenece el libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String genero;

    /**
     *
     * Sinopsis o descripción del contenido del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String sinopsis;

    /**
     *
     * Precio establecido para la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private double precio;

    /**
     *
     * Estado actual de la publicación dentro del sistema.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String estado;

    /**
     *
     * Cantidad disponible de ejemplares de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int cantidad;

    /**
     *
     * Indica si la publicación está disponible para venta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int esVenta;

    /**
     *
     * Indica si la publicación está disponible para renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int esRenta;

    /**
     *
     * Fecha en la que se realizó la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String fechaPublicacion;

    /**
     *
     * Imagen principal utilizada para mostrar la portada del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String imagenPrincipal;

    /**
     *
     * Imagen correspondiente al reverso del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String imagenReverso;

    /**
     *
     * Imagen correspondiente al interior del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String imagenInterior;

    /**
     *
     * Este constructor permite crear un objeto PublicacionAdminCompleta
     * sin proporcionar valores iniciales.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public PublicacionAdminCompleta() {
    }

    /**
     *
     * Este método obtiene el identificador de la publicación de LibriFlow.
     *
     * @return El identificador de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdPublicacionLf() { return idPublicacionLf; }

    /**
     *
     * Este método permite establecer el identificador de la publicación
     * de LibriFlow.
     *
     * @param idPublicacionLf Es el identificador que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setIdPublicacionLf(int idPublicacionLf) { this.idPublicacionLf = idPublicacionLf; }

    /**
     *
     * Este método obtiene el identificador de la publicación.
     *
     * @return El identificador de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdPublicacion() { return idPublicacionLf; }

    /**
     *
     * Este método permite establecer el identificador de la publicación.
     *
     * @param idPublicacion Es el identificador que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setIdPublicacion(int idPublicacion) { this.idPublicacionLf = idPublicacion; }

    /**
     *
     * Este método obtiene el identificador del libro relacionado
     * con la publicación.
     *
     * @return El identificador del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdLibro() { return idLibro; }

    /**
     *
     * Este método permite establecer el identificador del libro relacionado
     * con la publicación.
     *
     * @param idLibro Es el identificador del libro que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    /**
     *
     * Este método obtiene el título del libro.
     *
     * @return El título del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getTitulo() { return titulo; }

    /**
     *
     * Este método permite establecer el título del libro.
     *
     * @param titulo Es el título que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /**
     *
     * Este método obtiene el nombre del autor del libro.
     *
     * @return El autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getAutor() { return autor; }

    /**
     *
     * Este método permite establecer el nombre del autor del libro.
     *
     * @param autor Es el nombre del autor que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setAutor(String autor) { this.autor = autor; }

    /**
     *
     * Este método obtiene la editorial correspondiente al libro.
     *
     * @return La editorial del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getEditorial() { return editorial; }

    /**
     *
     * Este método permite establecer la editorial del libro.
     *
     * @param editorial Es la editorial que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEditorial(String editorial) { this.editorial = editorial; }

    /**
     *
     * Este método obtiene el género al que pertenece el libro.
     *
     * @return El género del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getGenero() { return genero; }

    /**
     *
     * Este método permite establecer el género del libro.
     *
     * @param genero Es el género que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setGenero(String genero) { this.genero = genero; }

    /**
     *
     * Este método obtiene la sinopsis de la publicación.
     *
     * @return La sinopsis del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getSinopsis() { return sinopsis; }

    /**
     *
     * Este método permite establecer la sinopsis del libro.
     *
     * @param sinopsis Es la sinopsis que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    /**
     *
     * Este método obtiene el precio establecido para la publicación.
     *
     * @return El precio de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public double getPrecio() { return precio; }

    /**
     *
     * Este método permite establecer el precio de la publicación.
     *
     * @param precio Es el precio que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     *
     * Este método obtiene el estado actual de la publicación.
     *
     * @return El estado de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getEstado() { return estado; }

    /**
     *
     * Este método permite establecer el estado de la publicación.
     *
     * @param estado Es el estado que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     *
     * Este método obtiene la cantidad disponible de ejemplares.
     *
     * @return La cantidad disponible.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getCantidad() { return cantidad; }

    /**
     *
     * Este método permite establecer la cantidad disponible
     * de ejemplares de la publicación.
     *
     * @param cantidad Es la cantidad que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    /**
     *
     * Este método obtiene el valor que indica si la publicación
     * está disponible para venta.
     *
     * @return El valor correspondiente a la disponibilidad para venta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getEsVenta() { return esVenta; }

    /**
     *
     * Este método permite establecer si la publicación se encuentra
     * disponible para venta.
     *
     * @param esVenta Es el valor que indica la disponibilidad para venta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEsVenta(int esVenta) { this.esVenta = esVenta; }

    /**
     *
     * Este método obtiene el valor que indica si la publicación
     * está disponible para renta.
     *
     * @return El valor correspondiente a la disponibilidad para renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getEsRenta() { return esRenta; }

    /**
     *
     * Este método permite establecer si la publicación se encuentra
     * disponible para renta.
     *
     * @param esRenta Es el valor que indica la disponibilidad para renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEsRenta(int esRenta) { this.esRenta = esRenta; }

    /**
     *
     * Este método obtiene la fecha en la que se realizó la publicación.
     *
     * @return La fecha de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getFechaPublicacion() { return fechaPublicacion; }

    /**
     *
     * Este método permite establecer la fecha en la que se realizó
     * la publicación.
     *
     * @param fechaPublicacion Es la fecha que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    /**
     *
     * Este método obtiene la imagen principal de la publicación.
     *
     * @return La imagen principal del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getImagenPrincipal() { return imagenPrincipal; }

    /**
     *
     * Este método permite establecer la imagen principal
     * de la publicación.
     *
     * @param imagenPrincipal Es la imagen principal que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    /**
     *
     * Este método obtiene la imagen correspondiente al reverso del libro.
     *
     * @return La imagen del reverso del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getImagenReverso() { return imagenReverso; }

    /**
     *
     * Este método permite establecer la imagen correspondiente
     * al reverso del libro.
     *
     * @param imagenReverso Es la imagen del reverso que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setImagenReverso(String imagenReverso) { this.imagenReverso = imagenReverso; }

    /**
     *
     * Este método obtiene la imagen correspondiente al interior del libro.
     *
     * @return La imagen del interior del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getImagenInterior() { return imagenInterior; }

    /**
     *
     * Este método permite establecer la imagen correspondiente
     * al interior del libro.
     *
     * @param imagenInterior Es la imagen interior que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setImagenInterior(String imagenInterior) { this.imagenInterior = imagenInterior; }
}