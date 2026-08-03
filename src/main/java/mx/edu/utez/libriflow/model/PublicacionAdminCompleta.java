package mx.edu.utez.libriflow.model;

public class PublicacionAdminCompleta {
    private int idPublicacionLf;
    private int idLibro;

    private String titulo;
    private String autor;
    private String editorial;
    private String genero;

    private String sinopsis;
    private double precio;
    private String estado;
    private int cantidad;
    private int esVenta;
    private int esRenta;
    private String fechaPublicacion;

    private String imagenPrincipal;
    private String imagenReverso;
    private String imagenInterior;

    public PublicacionAdminCompleta() {
    }

    public int getIdPublicacionLf() { return idPublicacionLf; }
    public void setIdPublicacionLf(int idPublicacionLf) { this.idPublicacionLf = idPublicacionLf; }

    public int getIdPublicacion() { return idPublicacionLf; }
    public void setIdPublicacion(int idPublicacion) { this.idPublicacionLf = idPublicacion; }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getEsVenta() { return esVenta; }
    public void setEsVenta(int esVenta) { this.esVenta = esVenta; }

    public int getEsRenta() { return esRenta; }
    public void setEsRenta(int esRenta) { this.esRenta = esRenta; }

    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    public String getImagenReverso() { return imagenReverso; }
    public void setImagenReverso(String imagenReverso) { this.imagenReverso = imagenReverso; }

    public String getImagenInterior() { return imagenInterior; }
    public void setImagenInterior(String imagenInterior) { this.imagenInterior = imagenInterior; }
}