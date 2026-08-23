package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;

/**
 *
 * Esta clase representa un movimiento realizado dentro del sistema.
 * Permite almacenar información relacionada con el comprador, el tipo
 * de movimiento, la fecha, el libro involucrado, el precio y las
 * ganancias correspondientes a LibriFlow.
 *
 * @author Andres Gerardo Angelina Perez
 * @since 22/08/2026
 */
public class Movimiento {

    /**
     *
     * Nombre del usuario que realizó la compra.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String comprador;

    /**
     *
     * Tipo de movimiento realizado dentro del sistema.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String tipoMovimiento;

    /**
     *
     * Fecha en la que se realizó el movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private Timestamp fecha;

    /**
     *
     * Título del libro relacionado con el movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private String titulo;

    /**
     *
     * Precio correspondiente al movimiento realizado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private double precio;

    /**
     *
     * Indica si el movimiento corresponde a una publicación
     * administrada directamente por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private boolean esLibriFlow;

    /**
     *
     * Cantidad correspondiente a la ganancia obtenida por LibriFlow
     * en el movimiento realizado.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    private double ganaciaLibriflow;

    /**
     *
     * Este constructor permite crear un objeto Movimiento sin proporcionar
     * valores iniciales.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Movimiento() {
    }

    /**
     *
     * Este constructor permite crear un movimiento proporcionando el tipo
     * de movimiento, la fecha, el título del libro, el precio y si la
     * publicación pertenece a LibriFlow.
     *
     * @param tipoMovimiento Es el tipo de movimiento realizado.
     * @param fecha Es la fecha en la que se realizó el movimiento.
     * @param titulo Es el título del libro relacionado con el movimiento.
     * @param precio Es el precio correspondiente al movimiento.
     * @param esLibriFlow Indica si la publicación pertenece a LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Movimiento(String tipoMovimiento, Timestamp fecha, String titulo,
                      double precio, boolean esLibriFlow) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.titulo = titulo;
        this.precio = precio;
        this.esLibriFlow = esLibriFlow;
    }

    /**
     *
     * Este método obtiene el tipo de movimiento realizado.
     *
     * @return El tipo de movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    /**
     *
     * Este método permite establecer el tipo de movimiento realizado.
     *
     * @param tipoMovimiento Es el tipo de movimiento que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    /**
     *
     * Este método obtiene la fecha en la que se realizó el movimiento.
     *
     * @return La fecha del movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     *
     * Este método permite establecer la fecha en la que se realizó
     * el movimiento.
     *
     * @param fecha Es la fecha que se desea asignar al movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    /**
     *
     * Este método obtiene el título del libro relacionado con el movimiento.
     *
     * @return El título del libro.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * Este método permite establecer el título del libro relacionado
     * con el movimiento.
     *
     * @param titulo Es el título que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * Este método obtiene el precio correspondiente al movimiento.
     *
     * @return El precio del movimiento.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public double getPrecio() {
        return precio;
    }

    /**
     *
     * Este método permite establecer el precio correspondiente
     * al movimiento.
     *
     * @param precio Es el precio que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     *
     * Este método indica si la publicación relacionada con el movimiento
     * pertenece directamente a LibriFlow.
     *
     * @return true si la publicación pertenece a LibriFlow o false
     *         si pertenece a un usuario.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public boolean isEsLibriFlow() {
        return esLibriFlow;
    }

    /**
     *
     * Este método permite establecer si la publicación relacionada
     * con el movimiento pertenece a LibriFlow.
     *
     * @param esLibriFlow Indica si la publicación pertenece a LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setEsLibriFlow(boolean esLibriFlow) {
        this.esLibriFlow = esLibriFlow;
    }

    /**
     *
     * Este método obtiene la ganancia correspondiente a LibriFlow
     * por el movimiento realizado.
     *
     * @return La ganancia obtenida por LibriFlow.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public double getGanaciaLibriflow() {
        return ganaciaLibriflow;
    }

    /**
     *
     * Este método permite establecer la ganancia obtenida por LibriFlow
     * en el movimiento realizado.
     *
     * @param ganaciaLibriflow Es la cantidad de ganancia que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setGanaciaLibriflow(double ganaciaLibriflow) {
        this.ganaciaLibriflow = ganaciaLibriflow;
    }

    /**
     *
     * Este método obtiene el nombre del comprador relacionado
     * con el movimiento.
     *
     * @return El nombre del comprador.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public String getComprador() {
        return comprador;
    }

    /**
     *
     * Este método permite establecer el nombre del comprador relacionado
     * con el movimiento.
     *
     * @param comprador Es el nombre del comprador que se desea asignar.
     *
     * @author Andres Gerardo Angelina Perez
     * @since 22/08/2026
     */
    public void setComprador(String comprador) {
        this.comprador = comprador;
    }
}