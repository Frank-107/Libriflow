package mx.edu.utez.libriflow.model;

import java.time.LocalDateTime;

public class Resena {

    private int idResena;
    private int idUsuario;
    private int idPublicacionLf;
    private String comentario;
    private int calificacion;
    private LocalDateTime fecha;
    private String nombreUsuario;

    public Resena() {}

    public Resena(int idUsuario, int idPublicacionLf, String comentario, int calificacion) {
        this.idUsuario = idUsuario;
        this.idPublicacionLf = idPublicacionLf;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public int getIdResena() { return idResena; }
    public void setIdResena(int idResena) { this.idResena = idResena; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdPublicacionLf() { return idPublicacionLf; }
    public void setIdPublicacionLf(int idPublicacionLf) { this.idPublicacionLf = idPublicacionLf; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}