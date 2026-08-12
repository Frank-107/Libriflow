package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;

public class Movimiento {

    private String tipoMovimiento;
    private Timestamp fecha;
    private String titulo;
    private double precio;
    private boolean esLibriFlow;

    public Movimiento() {
    }

    public Movimiento(String tipoMovimiento, Timestamp fecha, String titulo,
                      double precio, boolean esLibriFlow) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.titulo = titulo;
        this.precio = precio;
        this.esLibriFlow = esLibriFlow;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEsLibriFlow() {
        return esLibriFlow;
    }

    public void setEsLibriFlow(boolean esLibriFlow) {
        this.esLibriFlow = esLibriFlow;
    }
}