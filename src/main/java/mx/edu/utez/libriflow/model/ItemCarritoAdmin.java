package mx.edu.utez.libriflow.model;

import java.sql.Time;
import java.sql.Timestamp;

public class ItemCarritoAdmin {
    private int idPublicacion;
    private String tipoOperacion; // "venta" o "renta"
    private double precio;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;

    public ItemCarritoAdmin() {}

    public ItemCarritoAdmin(int idPublicacion, String tipoOperacion, double precio) {
        this.idPublicacion = idPublicacion;
        this.tipoOperacion = tipoOperacion;
        this.precio = precio;
    }

    public int getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(int idPublicacion) { this.idPublicacion = idPublicacion; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

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