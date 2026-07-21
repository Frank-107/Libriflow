package mx.edu.utez.libriflow.model;

import org.eclipse.tags.shaded.org.apache.xpath.objects.XString;

public class PublicacionResumen {

    private int idPublicacion;
    private String titulo;
    private String autor;
    private String genero;
    private Double precio;
    private String imagenPrincipal;
    private String estado;
    private boolean esLibriFlow;

    public PublicacionResumen(int idPublicacion, String titulo, String autor, String genero, Double precio, String imagenPrincipal, boolean esLibriFlow, String estado) {
        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.precio = precio;
        this.imagenPrincipal = imagenPrincipal;
        this.esLibriFlow = esLibriFlow;
        this.estado = estado;
    }

    public PublicacionResumen() {

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
}
