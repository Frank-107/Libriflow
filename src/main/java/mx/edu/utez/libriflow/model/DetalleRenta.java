package mx.edu.utez.libriflow.model;

import java.sql.Timestamp;

public class DetalleRenta {

    private int idDetalle;
    private Timestamp fechaInicio;
    private Timestamp fechaLimite;
    private Timestamp fechaDevolucion;
    private String estado;

    public DetalleRenta() {
    }

    public DetalleRenta(int idDetalle, Timestamp fechaInicio, Timestamp fechaLimite,
                        Timestamp fechaDevolucion, String estado) {
        this.idDetalle = idDetalle;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Timestamp fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Timestamp getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Timestamp fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}