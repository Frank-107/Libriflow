package mx.edu.utez.libriflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * La clase CompraResumen es una clase modelo que representa el resumen
 * con la información detallada de una compra realizada por un usuario.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 21/08/2026
 */
public class CompraResumen {

    private int idDetalle;
    private int idTransaccion;
    private int idPublicacion;
    private boolean esLibriFlow;
    private String titulo;
    private String autor;
    private String imagenPrincipal;
    private double precio;
    private String nombreVendedor;
    private LocalDateTime fecha;
    private String estadoTransaccion;

    /**
     * El método getFechaFormateada sirve para obtener la fecha de la transacción en formato "dd/MM/yyyy HH:mm".
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Cadena de texto con la fecha formateada o vacía si la fecha es nula
     */
    public String getFechaFormateada() {
        if (fecha == null) {
            return "";
        }

        return fecha.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        );
    }

    /**
     * El método getIdDetalle sirve para obtener el identificador único del detalle de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return int - Identificador único del detalle
     */
    public int getIdDetalle() {
        return idDetalle;
    }

    /**
     * El método setIdDetalle sirve para asignar el identificador único del detalle de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param idDetalle - Tipo: int, Identificador único del detalle
     */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * El método getIdTransaccion sirve para obtener el identificador de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return int - Identificador de la transacción
     */
    public int getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * El método setIdTransaccion sirve para asignar el identificador de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param idTransaccion - Tipo: int, Identificador de la transacción
     */
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * El método getIdPublicacion sirve para obtener el identificador de la publicación comprada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return int - Identificador de la publicación
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * El método setIdPublicacion sirve para asignar el identificador de la publicación comprada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param idPublicacion - Tipo: int, Identificador de la publicación
     */
    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    /**
     * El método isEsLibriFlow sirve para verificar si la publicación pertenece directamente a LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return boolean - true si la publicación es de LibriFlow o false si es de un usuario
     */
    public boolean isEsLibriFlow() {
        return esLibriFlow;
    }

    /**
     * El método setEsLibriFlow sirve para establecer si la publicación pertenece directamente a LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param esLibriFlow - Tipo: boolean, Indicador de origen LibriFlow
     */
    public void setEsLibriFlow(boolean esLibriFlow) {
        this.esLibriFlow = esLibriFlow;
    }

    /**
     * El método getTitulo sirve para obtener el título del libro o publicación comprada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Título de la publicación
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * El método setTitulo sirve para asignar el título del libro o publicación comprada.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param titulo - Tipo: String, Título de la publicación
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * El método getAutor sirve para obtener el nombre del autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Nombre del autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * El método setAutor sirve para asignar el nombre del autor del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param autor - Tipo: String, Nombre del autor del libro
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * El método getImagenPrincipal sirve para obtener la ruta o nombre de la imagen principal.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Nombre o ruta de la imagen principal
     */
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    /**
     * El método setImagenPrincipal sirve para asignar la ruta o nombre de la imagen principal.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param imagenPrincipal - Tipo: String, Nombre o ruta de la imagen principal
     */
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    /**
     * El método getPrecio sirve para obtener el precio de venta acordado en la compra.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return double - Precio de la compra
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * El método setPrecio sirve para asignar el precio de venta acordado en la compra.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param precio - Tipo: double, Precio de la compra
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * El método getNombreVendedor sirve para obtener el nombre del vendedor de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Nombre del vendedor o LibriFlow
     */
    public String getNombreVendedor() {
        return nombreVendedor;
    }

    /**
     * El método setNombreVendedor sirve para asignar el nombre del vendedor de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param nombreVendedor - Tipo: String, Nombre del vendedor
     */
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    /**
     * El método getFecha sirve para obtener la fecha y hora exacta en que se realizó la compra.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return LocalDateTime - Objeto con la fecha y hora de la transacción
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * El método setFecha sirve para asignar la fecha y hora exacta en que se realizó la compra.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param fecha - Tipo: LocalDateTime, Objeto con la fecha y hora de la transacción
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * El método getEstadoTransaccion sirve para obtener el estado actual de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @return String - Estado de la transacción
     */
    public String getEstadoTransaccion() {
        return estadoTransaccion;
    }

    /**
     * El método setEstadoTransaccion sirve para asignar el estado actual de la transacción.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 21/08/2026
     *
     * @param estadoTransaccion - Tipo: String, Estado de la transacción
     */
    public void setEstadoTransaccion(String estadoTransaccion) {
        this.estadoTransaccion = estadoTransaccion;
    }
}