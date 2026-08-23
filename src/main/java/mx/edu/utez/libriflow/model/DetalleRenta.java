package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;
/**
 * La clase DetalleRenta representa la entidad de modelo asociada al desglose
 * y seguimiento de una transacción de renta de libros dentro del sistema LibriFlow.
 * Almacena información sobre plazos, fechas efectivas, estados del servicio,
 * códigos de verificación y posibles penalizaciones acumuladas.
 *
 * @author Fuentes Perez Francisco Emmanuel
 * @since 23/08/2026
 */
public class DetalleRenta {

    private int idDetalle;
    private Timestamp fechaInicio;
    private Timestamp fechaLimite;
    private Timestamp fechaDevolucion;
    private String estado;
    private String codigo;
    private int penalizacion;
    private int idDetalleTransaccion;
    /**
     * Constructor predeterminado de la clase DetalleRenta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     */
    public DetalleRenta() {
    }
    /**
     * Constructor parametrizado para la creación e inicialización de un
     * registro básico de detalle de renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idDetalle Identificador único del detalle de renta.
     * @param fechaInicio Fecha y hora oficial en que inicia la renta.
     * @param fechaLimite Fecha y hora límite acordada para la entrega del libro.
     * @param fechaDevolucion Fecha y hora real en que fue devuelto el ejemplar.
     * @param estado Estado actual del proceso de renta (ej. Activo, Devuelto, Vencido).
     */
    public DetalleRenta(int idDetalle, Timestamp fechaInicio, Timestamp fechaLimite,
                        Timestamp fechaDevolucion, String estado) {
        this.idDetalle = idDetalle;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único del detalle de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return El ID numérico del detalle.
     */
    public int getIdDetalle() {
        return idDetalle;
    }

    /**
     * Establece el identificador único del detalle de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idDetalle ID numérico a asignar.
     */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Obtiene la fecha y hora de inicio de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Timestamp con la fecha inicial.
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha y hora de inicio de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param fechaInicio Objeto Timestamp correspondiente al inicio de la renta.
     */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha y hora límite de devolución.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Timestamp con el tiempo límite permitido.
     */
    public Timestamp getFechaLimite() {
        return fechaLimite;
    }

    /**
     * Establece la fecha y hora límite de devolución del libro.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param fechaLimite Objeto Timestamp correspondiente al límite establecido.
     */
    public void setFechaLimite(Timestamp fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    /**
     * Obtiene la fecha y hora en que se registró la devolución física.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Objeto Timestamp de la devolución efctuada.
     */
    public Timestamp getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * Registra la fecha y hora en que el libro fue entregado de vuelta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param fechaDevolucion Objeto Timestamp con la fecha de retorno.
     */
    public void setFechaDevolucion(Timestamp fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Obtiene el estado operativo actual de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Cadena de texto con la denominación del estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado operativo del registro de renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param estado Cadena descriptiva del nuevo estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el código alfanumérico o token asignado para la entrega o validación de la renta.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Cadena de texto con el código identificador.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Asigna el código alfanumérico o token para validaciones del servicio.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param codigo Código alfanumérico a asociar.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el monto numérico acumulado por concepto de multa o penalización por retraso.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return Valor entero con el monto de la penalización.
     */
    public int getPenalizacion() {
        return penalizacion;
    }

    /**
     * Define el monto o valor numérico de la penalización correspondiente al usuario.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param penalizacion Valor entero de la sanción económica aplicable.
     */
    public void setPenalizacion(int penalizacion) {
        this.penalizacion = penalizacion;
    }

    /**
     * Obtiene la clave foránea vinculada a la transacción global o maestro.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @return ID entero del detalle de transacción.
     */
    public int getIdDetalleTransaccion() {
        return idDetalleTransaccion;
    }

    /**
     * Asocia este registro de renta al identificador correspondiente en la transacción general.
     *
     * @author Fuentes Perez Francisco Emmanuel
     * @since 23/08/2026
     *
     * @param idDetalleTransaccion Identificador clave de la transacción asociada.
     */
    public void setIdDetalleTransaccion(int idDetalleTransaccion) {
        this.idDetalleTransaccion = idDetalleTransaccion;
    }
}