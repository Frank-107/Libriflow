package mx.edu.utez.libriflow.model;

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

    public DetalleTransaccion() {
    }

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

    public int getIdDetalle() {
        return idDetalle;
    }

    public double getGananciaVendedor() {
        return gananciaVendedor;
    }

    public void setGananciaVendedor(double gananciaVendedor) {
        this.gananciaVendedor = gananciaVendedor;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Integer getIdPublicacionUs() {
        return idPublicacionUs;
    }

    public void setIdPublicacionUs(Integer idPublicacionUs) {
        this.idPublicacionUs = idPublicacionUs;
    }

    public Integer getIdPublicacionLf() {
        return idPublicacionLf;
    }

    public void setIdPublicacionLf(Integer idPublicacionLf) {
        this.idPublicacionLf = idPublicacionLf;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getGananciaLibriFlow() {
        return gananciaLibriFlow;
    }

    public void setGananciaLibriFlow(double gananciaLibriFlow) {
        this.gananciaLibriFlow = gananciaLibriFlow;
    }
}
