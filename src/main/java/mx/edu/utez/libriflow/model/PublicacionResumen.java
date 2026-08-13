package mx.edu.utez.libriflow.model;

public class PublicacionResumen {

    private int idPublicacion;
    private String titulo;
    private String autor;
    private String genero;
    private Double precio;
    private String imagenPrincipal;
    private String estado;
    private String nombrePropietario;
    private int idPropietario;
    private boolean esLibriFlow;
    private boolean esRentaSeleccionada;
    private Double precioRentaCalculado;
    private int cantidad;

    public PublicacionResumen() {
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public boolean isEsLibriFlow() {
        return esLibriFlow;
    }

    public void setEsLibriFlow(boolean esLibriFlow) {
        this.esLibriFlow = esLibriFlow;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public boolean isEsRentaSeleccionada() {
        return esRentaSeleccionada;
    }

    public void setEsRentaSeleccionada(boolean esRentaSeleccionada) {
        this.esRentaSeleccionada = esRentaSeleccionada;
    }

    public Double getPrecioRentaCalculado() {
        return precioRentaCalculado;
    }

    public void setPrecioRentaCalculado(Double precioRentaCalculado) {
        this.precioRentaCalculado = precioRentaCalculado;
    }
}