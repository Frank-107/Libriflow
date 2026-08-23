package mx.edu.utez.libriflow.model;

/**
 * Clase modelo que representa una transacción o compra realizada dentro del sistema LibriFlow.
 * Almacena los datos generales del pago, costos de envío, montos finales y estado.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @since 22/08/2026
 */
public class Transaccion {

    private int idTransaccion;
    private int idComprador;
    private double subtotal;
    private double costoEnvio;
    private double total;
    private String estado;

    /**
     * Constructor por defecto sin parámetros.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Transaccion() {
    }

    /**
     * Constructor para registrar una nueva transacción (sin ID, para inserción en Base de Datos).
     *
     * @param idComprador Identificador único del usuario comprador.
     * @param subtotal Suma total de los productos antes de envío.
     * @param costoEnvio Monto correspondiente al costo de envío.
     * @param total Monto final a pagar.
     * @param estado Estado actual de la transacción (ej. 'COMPLETADA', 'PENDIENTE').
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Transaccion(int idComprador, double subtotal,
                       double costoEnvio, double total, String estado) {
        this.idComprador = idComprador;
        this.subtotal = subtotal;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.estado = estado;
    }

    /**
     * Constructor completo con todos los atributos de la transacción (para lecturas/consultas de BD).
     *
     * @param idTransaccion Identificador único autogenerado de la transacción.
     * @param idComprador Identificador único del usuario comprador.
     * @param subtotal Suma total de los productos antes de envío.
     * @param costoEnvio Monto correspondiente al costo de envío.
     * @param total Monto final pagado.
     * @param estado Estado actual de la transacción.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public Transaccion(int idTransaccion, int idComprador,
                       double subtotal, double costoEnvio, double total,
                       String estado) {
        this.idTransaccion = idTransaccion;
        this.idComprador = idComprador;
        this.subtotal = subtotal;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único de la transacción.
     *
     * @return El id de la transacción.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public int getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Establece el identificador único de la transacción.
     *
     * @param idTransaccion El nuevo id de la transacción.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * Obtiene el identificador del usuario comprador.
     *
     * @return El id del comprador.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public int getIdComprador() {
        return idComprador;
    }

    /**
     * Establece el identificador del usuario comprador.
     *
     * @param idComprador El id del usuario comprador.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }

    /**
     * Obtiene el subtotal acumulado de los items comprados.
     *
     * @return El subtotal de la compra.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal acumulado de los items comprados.
     *
     * @param subtotal El nuevo subtotal.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el costo del servicio de envío.
     *
     * @return El costo del envío.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public double getCostoEnvio() {
        return costoEnvio;
    }

    /**
     * Establece el costo del servicio de envío.
     *
     * @param costoEnvio El nuevo costo de envío.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    /**
     * Obtiene el total cobrado en la transacción.
     *
     * @return El total final.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el total cobrado en la transacción.
     *
     * @param total El nuevo total final.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Obtiene el estado actual de la transacción.
     *
     * @return El estado de la transacción.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual de la transacción.
     *
     * @param estado El nuevo estado de la transacción.
     *
     * @author Francisco Emmanuel Fuentes Pérez
     * @since 22/08/2026
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}