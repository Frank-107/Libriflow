package mx.edu.utez.libriflow.model;


import java.sql.Timestamp;

/**
 *
 * Esta clase representa un elemento del carrito correspondiente a una
 * publicación administrada por LibriFlow. Almacena la información necesaria
 * para identificar la publicación, el tipo de operación, el precio y las
 * fechas utilizadas cuando se realiza una renta.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
public class ItemCarritoAdmin {

    /**
     *
     * Identificador de la publicación agregada al carrito.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private int idPublicacion;

    /**
     *
     * Tipo de operación que se realizará con la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String tipoOperacion; // "venta" o "renta"

    /**
     *
     * Precio correspondiente a la publicación según el tipo de operación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private double precio;

    /**
     *
     * Fecha en la que inicia la renta de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private Timestamp fechaInicio;

    /**
     *
     * Fecha en la que finaliza la renta de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private Timestamp fechaFin;

    /**
     *
     * Constructor vacío utilizado para crear un elemento del carrito
     * sin proporcionar valores iniciales.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public ItemCarritoAdmin() {}

    /**
     *
     * Este constructor permite crear un elemento del carrito proporcionando
     * el identificador de la publicación, el tipo de operación y el precio
     * correspondiente.
     *
     * @param idPublicacion Es el identificador de la publicación.
     * @param tipoOperacion Es el tipo de operación que se realizará.
     * @param precio Es el precio correspondiente a la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public ItemCarritoAdmin(int idPublicacion, String tipoOperacion, double precio) {
        this.idPublicacion = idPublicacion;
        this.tipoOperacion = tipoOperacion;
        this.precio = precio;
    }

    /**
     *
     * Este método obtiene el identificador de la publicación almacenada
     * en el carrito.
     *
     * @return El identificador de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public int getIdPublicacion() { return idPublicacion; }

    /**
     *
     * Este método permite establecer el identificador de la publicación.
     *
     * @param idPublicacion Es el identificador de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setIdPublicacion(int idPublicacion) { this.idPublicacion = idPublicacion; }

    /**
     *
     * Este método obtiene el tipo de operación seleccionado para
     * la publicación.
     *
     * @return El tipo de operación de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getTipoOperacion() { return tipoOperacion; }

    /**
     *
     * Este método permite establecer el tipo de operación que se realizará
     * con la publicación.
     *
     * @param tipoOperacion Es el tipo de operación que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    /**
     *
     * Este método obtiene el precio correspondiente a la publicación
     * almacenada en el carrito.
     *
     * @return El precio de la publicación.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public double getPrecio() { return precio; }

    /**
     *
     * Este método permite establecer el precio correspondiente
     * a la publicación.
     *
     * @param precio Es el precio que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     *
     * Este método obtiene la fecha de inicio establecida para la renta
     * de la publicación.
     *
     * @return La fecha de inicio de la renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    /**
     *
     * Este método permite establecer la fecha de inicio de la renta
     * de la publicación.
     *
     * @param fechaInicio Es la fecha en la que comenzará la renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     *
     * Este método obtiene la fecha de finalización establecida para
     * la renta de la publicación.
     *
     * @return La fecha de finalización de la renta.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Timestamp getFechaFin() {
        return fechaFin;
    }

    /**
     *
     * Este método permite establecer la fecha de finalización de una renta.
     * Cuando se proporciona una fecha válida, ajusta la hora a las 23:59:59
     * del mismo día. Si la fecha recibida es nula, el valor se establece
     * como nulo.
     *
     * @param fechaFin Es la fecha de finalización que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setFechaFin(Timestamp fechaFin) {
        if (fechaFin != null) {
            this.fechaFin = Timestamp.valueOf(
                    fechaFin.toLocalDateTime()
                            .withHour(23)
                            .withMinute(59)
                            .withSecond(59)
                            .withNano(0)
            );
        } else {
            this.fechaFin = null;
        }
    }
}