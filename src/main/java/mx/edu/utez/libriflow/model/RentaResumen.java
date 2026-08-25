package mx.edu.utez.libriflow.model;

import java.time.LocalDate;

/**
 * Clase de modelo (DTO/Entity) que representa el resumen consolidado de una renta de libro.
 * Encapsula la información clave del préstamo, incluyendo datos de la transacción,
 * detalles del libro, involucrados (comprador/vendedor), fechas operativas, vigencia y estados.
 *
 * @author Andres
 * @since 24/08/2026
 */
public class RentaResumen {

    /** Identificador único del detalle de la transacción/renta. */
    private int idDetalle;

    /** Identificador de la transacción general asociada. */
    private int idTransaccion;

    /** Código único de seguimiento o comprobante de la renta. */
    private String codigo;

    /** Monto o penalización económica acumulada por retraso en la devolución. */
    private int penalizacion;

    /** Título del libro rentado. */
    private String titulo;

    /** Autor del libro rentado. */
    private String autor;

    /** Ruta o URL de la imagen de portada principal del libro. */
    private String imagenPrincipal;

    /** Precio o tarifa aplicada a la renta del libro. */
    private double precio;

    /** Nombre completo del usuario que efectúa la renta (cliente/comprador). */
    private String nombreComprador;

    /** Nombre completo del usuario arrendador o propietario del libro. */
    private String nombreVendedor;

    /** Fecha en la que da inicio el periodo de renta. */
    private LocalDate fechaInicio;

    /** Fecha límite permitida para efectuar la devolución del libro. */
    private LocalDate fechaLimite;

    /** Fecha real en la que el usuario realizó la devolución del libro. */
    private LocalDate fechaDevolucion;

    /** Estado operativo actual de la renta (ej. ACTIVO, ENTREGADO, FINALIZADO). */
    private String estado;

    /** Cantidad de días restantes antes de alcanzar la fecha límite de devolución. */
    private int diasRestantes;

    /**
     * Obtiene el identificador del detalle de la renta.
     *
     * @return Entero con el ID del detalle.
     */
    public int getIdDetalle() {
        return idDetalle;
    }

    /**
     * Establece el identificador del detalle de la renta.
     *
     * @param idDetalle Nuevo identificador del detalle.
     */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Obtiene el identificador de la transacción.
     *
     * @return Entero con el ID de la transacción.
     */
    public int getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Establece el identificador de la transacción.
     *
     * @param idTransaccion Nuevo identificador de la transacción.
     */
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * Obtiene el código de seguimiento de la renta.
     *
     * @return Cadena con el código de seguimiento.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código de seguimiento de la renta.
     *
     * @param codigo Código alfanumérico de referencia.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la penalización económica.
     *
     * @return Entero con el monto de la penalización.
     */
    public int getPenalizacion() {
        return penalizacion;
    }

    /**
     * Establece la penalización económica por demora.
     *
     * @param penalizacion Monto acumulado de la penalización.
     */
    public void setPenalizacion(int penalizacion) {
        this.penalizacion = penalizacion;
    }

    /**
     * Obtiene el título del libro rentado.
     *
     * @return Cadena con el título.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro rentado.
     *
     * @param titulo Título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el nombre del autor del libro.
     *
     * @return Cadena con el nombre del autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el nombre del autor del libro.
     *
     * @param autor Nombre del autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene la ruta de la imagen principal.
     *
     * @return Cadena con la URL o ruta del recurso de imagen.
     */
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    /**
     * Establece la ruta de la imagen principal del libro.
     *
     * @param imagenPrincipal Ruta de acceso a la portada.
     */
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    /**
     * Obtiene el precio asignado a la renta.
     *
     * @return Valor numérico decimal con el precio.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio correspondiente a la renta.
     *
     * @param precio Monto asignado.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el nombre del usuario arrendatario / comprador.
     *
     * @return Cadena con el nombre completo.
     */
    public String getNombreComprador() {
        return nombreComprador;
    }

    /**
     * Establece el nombre del usuario arrendatario / comprador.
     *
     * @param nombreComprador Nombre del usuario.
     */
    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    /**
     * Obtiene el nombre del usuario arrendador / vendedor.
     *
     * @return Cadena con el nombre completo.
     */
    public String getNombreVendedor() {
        return nombreVendedor;
    }

    /**
     * Establece el nombre del usuario arrendador / vendedor.
     *
     * @param nombreVendedor Nombre del usuario.
     */
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    /**
     * Obtiene la fecha de inicio del préstamo.
     *
     * @return Objeto {@link LocalDate} con la fecha inicial.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del préstamo.
     *
     * @param fechaInicio Objeto {@link LocalDate} indicando el comienzo.
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha límite programada para la devolución.
     *
     * @return Objeto {@link LocalDate} con la fecha límite.
     */
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    /**
     * Establece la fecha límite para realizar la devolución.
     *
     * @param fechaLimite Objeto {@link LocalDate} con el límite máximo.
     */
    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    /**
     * Obtiene la fecha real de devolución registrada.
     *
     * @return Objeto {@link LocalDate} con la fecha devuelta.
     */
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * Establece la fecha real en la que fue devuelto el libro.
     *
     * @param fechaDevolucion Objeto {@link LocalDate} con la fecha de entrega.
     */
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Obtiene el estado actual de la renta.
     *
     * @return Cadena con el estado operativo.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado operativo de la renta.
     *
     * @param estado Nuevo estado (ej. ACTIVO, FINALIZADO).
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la cantidad de días restantes para la devolución.
     *
     * @return Entero representando los días de plazo.
     */
    public int getDiasRestantes() {
        return diasRestantes;
    }

    /**
     * Establece la cantidad de días restantes de préstamo.
     *
     * @param diasRestantes Días calculados hasta la fecha límite.
     */
    public void setDiasRestantes(int diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    /**
     * Evalúa si la entrega del libro puede ser habilitada operativamente.
     * Verifica que la fecha de inicio no sea nula y que la fecha actual sea igual o posterior
     * a la fecha de inicio especificada.
     *
     * @return {@code true} si la fecha actual es igual o posterior a la fecha de inicio; {@code false} de lo contrario.
     */
    public boolean isPuedeEntregar() {
        return fechaInicio != null && !LocalDate.now().isBefore(fechaInicio);
    }
}