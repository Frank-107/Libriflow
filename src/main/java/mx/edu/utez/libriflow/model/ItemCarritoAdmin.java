package mx.edu.utez.libriflow.model;

public class ItemCarritoAdmin {
    private int idPublicacion;
    private String tipoOperacion; // "venta" o "renta"
    private double precio;

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
}
