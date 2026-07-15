package mx.edu.utez.libriflow.model;

public class PublicacionUsuario {
    private int idPublicacionUs;
    private int idUsuario;
    private int idLibro;

    private String fechaPublicacion;
    private String estado;
    private String tipoServicio;
    private double precio;


    // Constructor vacío
    public PublicacionUsuario() {
    }


    // Constructor sin ID (para crear una publicación nueva)
    public PublicacionUsuario(int idUsuario, int idLibro,
                              String fechaPublicacion,
                              String estado,
                              String tipoServicio,
                              double precio) {

        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaPublicacion = fechaPublicacion;
        this.estado = estado;
        this.tipoServicio = tipoServicio;
        this.precio = precio;
    }


    // Constructor completo
    public PublicacionUsuario(int idPublicacionUs,
                              int idUsuario,
                              int idLibro,
                              String fechaPublicacion,
                              String estado,
                              String tipoServicio,
                              double precio) {

        this.idPublicacionUs = idPublicacionUs;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaPublicacion = fechaPublicacion;
        this.estado = estado;
        this.tipoServicio = tipoServicio;
        this.precio = precio;
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


    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

