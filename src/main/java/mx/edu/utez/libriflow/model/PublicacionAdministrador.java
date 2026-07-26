package mx.edu.utez.libriflow.model;

public class PublicacionAdministrador {
    private int idPublicacionLf;
    private int idLibro;
    private String fechaPublicacion;
    private String estado;
    private int cantidad;
    private String sinopsis;
    private int esVenta;
    private int esRenta;
    private double precio;

    public PublicacionAdministrador() {
    }

    public PublicacionAdministrador(int idPublicacionLf, int idLibro, String fechaPublicacion, String estado, int cantidad, String sinopsis, int esVenta, int esRenta, double precio) {
        this.idPublicacionLf = idPublicacionLf;
        this.idLibro = idLibro;
        this.fechaPublicacion = fechaPublicacion;
        this.estado = estado;
        this.cantidad = cantidad;
        this.sinopsis = sinopsis;
        this.esVenta = esVenta;
        this.esRenta = esRenta;
        this.precio = precio;
    }

    public int getIdPublicacionLf() {
        return idPublicacionLf;
    }

    public void setIdPublicacionLf(int idPublicacionLf) {
        this.idPublicacionLf = idPublicacionLf;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getEsVenta() {
        return esVenta;
    }

    public void setEsVenta(int esVenta) {
        this.esVenta = esVenta;
    }

    public int getEsRenta() {
        return esRenta;
    }

    public void setEsRenta(int esRenta) {
        this.esRenta = esRenta;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}