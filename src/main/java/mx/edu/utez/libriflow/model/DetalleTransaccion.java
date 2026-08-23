package mx.edu.utez.libriflow.model;

/**
 * La clase DetalleTransaccion representa la entidad de modelo encargada de
 * gestionar el desglose individual de cada ítem u operación dentro de una
 * transacción global en LibriFlow.
 * Almacena información sobre publicaciones asociadas (de usuarios o propias de LibriFlow),
 * desgloses financieros (precio, ganancia del vendedor y comisiones de la plataforma)
 * y tipos de operación (compra o renta).
 *
 * @author Fuentes Perez Francisco Emmanuel
 * @since 23/08/2026
 */
public class DetalleTransaccion {
    private int idDetalle;
    private int idTransaccion;
    private Integer idPublicacionUs;
    private Integer idPublicacionLf;
    private double gananciaVendedor;
    private Integer idVendedor;
    private String tipoOperacion;
    private double precio;
    private double gananciaLibriFlow;

    /**
     * Constructor predeterminado de la clase DetalleTransaccion.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     */
    public DetalleTransaccion() {
    }

    /**
     * Constructor parametrizado para la creación e inicialización de un
     * detalle de transacción antes de ser asignado un ID de registro.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idTransaccion Clave foránea de la transacción maestro a la que pertenece.
     * @param idPublicacionUs Identificador de la publicación realizada por usuario (opcional).
     * @param idPublicacionLf Identificador de la publicación del catálogo LibriFlow (opcional).
     * @param idVendedor Identificador del usuario vendedor involucrado en la venta.
     * @param tipoOperacion Modalidad de la transacción (ej. Venta, Renta).
     * @param precio Monto comercial asignado a la publicación.
     * @param gananciaLibriFlow Comisión obtenida por la plataforma.
     */
    public DetalleTransaccion(int idTransaccion, Integer idPublicacionUs,
                              Integer idPublicacionLf, Integer idVendedor,
                              String tipoOperacion, double precio,
                              double gananciaLibriFlow) {
        this.idTransaccion = idTransaccion;
        this.idPublicacionUs = idPublicacionUs;
        this.idPublicacionLf = idPublicacionLf;
        this.idVendedor = idVendedor;
        this.tipoOperacion = tipoOperacion;
        this.precio = precio;
        this.gananciaLibriFlow = gananciaLibriFlow;
    }

    /**
     * Constructor parametrizado completo para la reconstrucción de un
     * registro proveniente de la base de datos.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idDetalle Identificador único del detalle de transacción.
     * @param idTransaccion Clave de la transacción general asociada.
     * @param idPublicacionUs ID de la publicación de usuario asociada.
     * @param idPublicacionLf ID de la publicación de LibriFlow asociada.
     * @param idVendedor ID del usuario registrado como vendedor.
     * @param tipoOperacion Tipo o modalidad del servicio.
     * @param precio Precio final asignado.
     * @param gananciaLibriFlow Monto destinado a la comisión del sistema.
     */
    public DetalleTransaccion(int idDetalle, int idTransaccion,
                              Integer idPublicacionUs, Integer idPublicacionLf,
                              int idVendedor, String tipoOperacion,
                              double precio, double gananciaLibriFlow) {
        this.idDetalle = idDetalle;
        this.idTransaccion = idTransaccion;
        this.idPublicacionUs = idPublicacionUs;
        this.idPublicacionLf = idPublicacionLf;
        this.idVendedor = idVendedor;
        this.tipoOperacion = tipoOperacion;
        this.precio = precio;
        this.gananciaLibriFlow = gananciaLibriFlow;
    }

    /**
     * Obtiene el identificador único del detalle de transacción.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Clave primaria numérica del detalle.
     */
    public int getIdDetalle() {
        return idDetalle;
    }

    /**
     * Obtiene el monto neto calculado para el vendedor de la publicación.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Valor numérico con la ganancia libre del vendedor.
     */
    public double getGananciaVendedor() {
        return gananciaVendedor;
    }

    /**
     * Asigna la cifra líquida o ganancia calculada para el usuario vendedor.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param gananciaVendedor Monto numérico correspondiente al vendedor.
     */
    public void setGananciaVendedor(double gananciaVendedor) {
        this.gananciaVendedor = gananciaVendedor;
    }

    /**
     * Establece el identificador único del detalle de transacción.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idDetalle Clave primaria numérica a asignar.
     */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Obtiene la clave foránea vinculada a la transacción principal.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return ID numérico de la transacción.
     */
    public int getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Asocia este detalle a una transacción general mediante su ID.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idTransaccion Clave principal de la transacción general.
     */
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * Obtiene el identificador de la publicación de usuario asociada.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Integer con el ID de la publicación o {@code null} si no aplica.
     */
    public Integer getIdPublicacionUs() {
        return idPublicacionUs;
    }

    /**
     * Establece la clave foránea correspondiente a la publicación hecha por un usuario.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idPublicacionUs Identificador numérico de la publicación de usuario.
     */
    public void setIdPublicacionUs(Integer idPublicacionUs) {
        this.idPublicacionUs = idPublicacionUs;
    }

    /**
     * Obtiene el identificador de la publicación original de LibriFlow.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Integer con el ID de la publicación institucional o {@code null}.
     */
    public Integer getIdPublicacionLf() {
        return idPublicacionLf;
    }

    /**
     * Define la clave foránea correspondiente a la publicación directa de LibriFlow.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idPublicacionLf Identificador numérico de la publicación de LibriFlow.
     */
    public void setIdPublicacionLf(Integer idPublicacionLf) {
        this.idPublicacionLf = idPublicacionLf;
    }

    /**
     * Obtiene el identificador del usuario con el rol de vendedor.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Integer con el ID del vendedor.
     */
    public Integer getIdVendedor() {
        return idVendedor;
    }

    /**
     * Registra al usuario que actúa como vendedor dentro de la transacción.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idVendedor Identificador numérico del vendedor.
     */
    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    /**
     * Obtiene el tipo o modalidad de la operación realizada.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Cadena descriptiva (ej. Venta, Renta).
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Define el tipo o categoría de operación efectuada.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param tipoOperacion Cadena con la categoría del servicio.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Obtiene el precio total o valor comercial del libro procesado.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Valor double con el precio del ítem.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Asigna el valor comercial o importe asignado al ítem de la transacción.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param precio Cifra decimal que representa el precio.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el monto neto cobrado por LibriFlow en concepto de comisión por la gestión.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Importe de ganancia para la plataforma.
     */
    public double getGananciaLibriFlow() {
        return gananciaLibriFlow;
    }

    /**
     * Establece el monto destinado como ganancia o comisión a favor de LibriFlow.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param gananciaLibriFlow Importe decimal de la comisión.
     */
    public void setGananciaLibriFlow(double gananciaLibriFlow) {
        this.gananciaLibriFlow = gananciaLibriFlow;
    }
}
