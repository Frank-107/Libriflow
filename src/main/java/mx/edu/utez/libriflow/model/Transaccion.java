package mx.edu.utez.libriflow.model;

public class Transaccion {
    private int idTransaccion;
    private int idComprador;
    private double subtotal;
    private double costoEnvio;
    private double total;
    private String estado;

    public Transaccion() {
    }

    public Transaccion(int idComprador, double subtotal,
                       double costoEnvio, double total, String estado) {
        this.idComprador = idComprador;
        this.subtotal = subtotal;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.estado = estado;
    }

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

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }


    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
