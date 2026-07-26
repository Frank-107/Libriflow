package mx.edu.utez.libriflow.model;

import java.time.LocalDateTime;

public class PublicacionUsuarioCompleta {
    private int idPublicacion;
    private int idPropietario;
    private int idLibro;

    private String titulo;
    private String autor;
    private String editorial;
    private String genero;

    private String sinopsis;
    private double precio;
    private String estado;
    private LocalDateTime fecha;

    private String imagenPrincipal;
    private String imagenReverso;
    private String imagenInterior;

    public PublicacionUsuarioCompleta() {
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public String getImagenReverso() {
        return imagenReverso;
    }

    public void setImagenReverso(String imagenReverso) {
        this.imagenReverso = imagenReverso;
    }

    public String getImagenInterior() {
        return imagenInterior;
    }

    public void setImagenInterior(String imagenInterior) {
        this.imagenInterior = imagenInterior;
    }
}
