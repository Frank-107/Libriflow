package mx.edu.utez.libriflow.model;

public class PublicacionUsuario {
    private int idPublicacionUs;
    private int idUsuario;
    private int idLibro;
    private String fechaPublicacion;
    private String estado;
    private double precio;
    private String sinopsis;


    public PublicacionUsuario() {
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getIdPublicacionUs() {
        return idPublicacionUs;
    }

    public void setIdPublicacionUs(int idPublicacionUs) {
        this.idPublicacionUs = idPublicacionUs;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

